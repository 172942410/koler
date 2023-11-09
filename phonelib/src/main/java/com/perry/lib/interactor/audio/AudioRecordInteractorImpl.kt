package com.perry.lib.interactor.audio

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager.STREAM_DTMF
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import com.perry.lib.interactor.base.BaseInteractorImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Arrays
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AudioRecordInteractorImpl @Inject constructor(
    private val context: Context,
    private val disposables: CompositeDisposable,
) :
    BaseInteractorImpl<AudioRecordInteractor.Listener>(),
    AudioRecordInteractor {
    val TAG = "AudioRecordInteractorImpl"
//    private val audioRecord: AudioRecord // 录音对象
    private val frequence: Int = 44100 // 采样率 8000 44100
    private val channelInConfig: Int = AudioFormat.CHANNEL_IN_STEREO // 定义采样通道
    private val audioEncoding: Int = AudioFormat.ENCODING_PCM_16BIT // 定义音频编码（16位）
//    private val audioEncoding: Int = AudioFormat.ENCODING_MP3 // 定义音频编码；这里获取不到缓存区域
//    private val buffer: ByteArray? = null // 录制的缓冲数组
    var isRecording = false
    val SD_PATH = Environment.getExternalStorageDirectory().absolutePath
    /**
     * 设置根目录下的子目录位置
     */
    val ROOT_PATH = "/app_lxd/"
    val RECORD_PATH: String = SD_PATH + ROOT_PATH + "audioRecord/"

    @SuppressLint("MissingPermission")
    override fun initRecord() {
//        ("开始调用记录语音流之前的对象初始化")
        // 根据定义好的几个配置，来获取合适的缓冲大小
        // int bufferSize = 800;
//        val bufferSize = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding)
        // 实例化AudioRecord
//        audioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelInConfig, audioEncoding, bufferSize)
        // 定义缓冲数组
//        buffer = ByteArray(bufferSize)
    }

//    override var bufferSize: Int
//        get() = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding)
//        set(value) {
//            bufferSize = value
//        }

//    override var audioRecord: AudioRecord
//        get() = AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelInConfig, audioEncoding, bufferSize)
//        set(value) {
//            audioRecord = value
//        }

//    override var buffer: ByteArray
//        get() = ByteArray(bufferSize)
//        set(value) {
//            buffer = value
//        }

    override var isSpeakerOn = false;
//        get() = audioRecord.isSpeakerphoneOn
//        set(value) {
//            audioRecord.isSpeakerphoneOn = value
//        }


    override fun startRecord() {
//        ("开始调用记录语音流")
        if(isRecording){
            Log.d(TAG,"startRecord已经调用开始了；无需再次调用了")
            return
        }
//        disposables.add()
        Observable.create<String>{
            Log.d(TAG,"frequence:" + frequence)
            Log.d(TAG,"channelInConfig:" + channelInConfig)
            Log.d(TAG,"audioEncoding:" + audioEncoding)
            var bufferSize: Int = AudioRecord.getMinBufferSize(frequence, channelInConfig, audioEncoding)
            Log.d(TAG,"bufferSize:" + bufferSize)
//            AudioRecord.Builder().setAudioSource(MediaRecorder.AudioSource.MIC)
//                .setBufferSizeInBytes(bufferSize)
            var audioRecord: AudioRecord = AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelInConfig, audioEncoding, bufferSize)
            Log.d(TAG,"audioRecord:" + audioRecord)
            audioRecord.startRecording() // 开始录制
            isRecording = true // 设置录制标记为true
            var filename = "通话录音" + System.currentTimeMillis() + ".wav"
            var filePath = File(RECORD_PATH)
            if(!filePath.exists()){
                filePath.mkdirs()
            }
            var file = File(filePath,filename)
            if(!file.exists()){
                file.createNewFile()
            }
            val os = FileOutputStream(file)
//            val os = context.openFileOutput(filename,Context.MODE_PRIVATE);
            Log.d(TAG,"创建录音文件；准开写录音文件头...")
            writeAudioHeader(os,channelInConfig,frequence,audioEncoding)
            Log.d(TAG,"录音头写完；准开始录音了...")
//        val os: FileOutputStream
//        try {
//            os = FileOutputStream(filename)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            isRecording = false
//            Log.d(TAG,"startRecord无法保存文件异常了")
//            return
//        }
//            var buffer: ByteBuffer = ByteBuffer(bufferSize)
            // 开始录制
            while (isRecording) {
                var buffer = ByteArray(bufferSize)
                // 录制的内容放置到了buffer中，result代表存储长度
                val result = audioRecord.read(buffer, 0, buffer.size)
                /*.....result为buffer中录制数据的长度(貌似基本上都是640)。
                剩下就是处理buffer了，是发送出去还是直接播放，这个随便你。*/
                os.write(buffer)
                Log.d(TAG,"result:"+result)
                Log.d(TAG,"buffer:"+ Arrays.toString(buffer))
//            if(result < 0){
//                os.close()
//                isRecording = false
//                break
//            }
                Thread.sleep(100)
            }
            //录制循环结束后，记得关闭录制！！
            if (audioRecord != null) {
                os.close()
                updateWavHeader(file)
                audioRecord.stop()
                audioRecord.release()
                audioRecord == null
            }
            it.onNext("完成了")
            it.onComplete()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer<String> {
                    onNext -> {
                        Log.d(TAG,"onNext:"+onNext)

            }
                                        },
                Consumer<Throwable>{
                    onError ->{
                        Log.d(TAG,"onError:"+onError)
                }
                },
                {
                    Log.d(TAG,"异步执行完成")
                }

                )

    }

    /**
     * Writes the proper 44-byte RIFF/WAVE header to/for the given stream
     * Two size fields are left empty/null since we do not yet know the final stream size
     * </br>
     * 将正确的44字节RIFF/WAVE标头写入给定流
     * 两个大小字段为空/空，因为我们还不知道最终的流大小
     *
     * @param out The stream to write the header to
     * @param channelMask An AudioFormat.CHANNEL_* mask
     * @param sampleRate The sample rate in hertz
     * @param encoding An AudioFormat.ENCODING_PCM_* value
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun writeAudioHeader(
        out: OutputStream,
        channelMask: Int,
        sampleRate: Int,
        encoding: Int
    ) {
        val channels: Short
        channels = when (channelMask) {
            AudioFormat.CHANNEL_IN_MONO -> 1
            AudioFormat.CHANNEL_IN_STEREO -> 2
            else -> throw IllegalArgumentException("不可接受的通道掩码（channel mask）：" + channelMask)
        }
        val bitDepth: Short
        bitDepth = when (encoding) {
            AudioFormat.ENCODING_PCM_8BIT -> 8
            AudioFormat.ENCODING_PCM_16BIT -> 16
            AudioFormat.ENCODING_PCM_FLOAT -> 32
            else -> throw IllegalArgumentException("音频编码错误（encoding）：" + encoding)
        }
        writeWavHeader(out, channels.toShort(), sampleRate, bitDepth.toShort())
    }

    /**
     * Writes the proper 44-byte RIFF/WAVE header to/for the given stream
     * Two size fields are left empty/null since we do not yet know the final stream size
     *
     * @param out The stream to write the header to
     * @param channels The number of channels
     * @param sampleRate The sample rate in hertz
     * @param bitDepth The bit depth
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun writeWavHeader(
        out: OutputStream,
        channels: Short,
        sampleRate: Int,
        bitDepth: Short
    ) {
        // Convert the multi-byte integers to raw bytes in little endian format as required by the spec
        val littleBytes = ByteBuffer.allocate(14)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putShort(channels)
            .putInt(sampleRate)
            .putInt(sampleRate * channels * (bitDepth / 8))
            .putShort((channels * (bitDepth / 8)).toShort())
            .putShort(bitDepth)
            .array()

        // Not necessarily the best, but it's very easy to visualize this way
        out.write(
            byteArrayOf( // RIFF header
                'R'.code.toByte(),
                'I'.code.toByte(),
                'F'.code.toByte(),
                'F'.code.toByte(),  // ChunkID
                0,
                0,
                0,
                0,  // ChunkSize (must be updated later)
                'W'.code.toByte(),
                'A'.code.toByte(),
                'V'.code.toByte(),
                'E'.code.toByte(),  // Format
                // fmt subchunk
                'f'.code.toByte(),
                'm'.code.toByte(),
                't'.code.toByte(),
                ' '.code.toByte(),  // Subchunk1ID
                16,
                0,
                0,
                0,  // Subchunk1Size
                1,
                0,  // AudioFormat
                littleBytes[0],
                littleBytes[1],  // NumChannels
                littleBytes[2],
                littleBytes[3],
                littleBytes[4],
                littleBytes[5],  // SampleRate
                littleBytes[6],
                littleBytes[7],
                littleBytes[8],
                littleBytes[9],  // ByteRate
                littleBytes[10],
                littleBytes[11],  // BlockAlign
                littleBytes[12],
                littleBytes[13],  // BitsPerSample
                // data subchunk
                'd'.code.toByte(),
                'a'.code.toByte(),
                't'.code.toByte(),
                'a'.code.toByte(),  // Subchunk2ID
                0,
                0,
                0,
                0
            )
        )
    }

    /**
     *
     * 更新给定wav文件的头，以包括最终的块大小
     *
     * @param wav 要更新的wav文件
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun updateWavHeader(wav: File) {
        val sizes = ByteBuffer.allocate(8)
            .order(ByteOrder.LITTLE_ENDIAN)
            // There are probably a bunch of different/better ways to calculate
            // these two given your circumstances. Cast should be safe since if the WAV is
            // > 4 GB we've already made a terrible mistake.
            .putInt((wav.length() - 8).toInt()) // ChunkSize
            .putInt((wav.length() - 44).toInt()) // Subchunk2Size
            .array()
        var accessWave: RandomAccessFile? = null
        try {
            accessWave = RandomAccessFile(wav, "rw")
            // ChunkSize
            accessWave.seek(4)
            accessWave.write(sizes, 0, 4)

            // Subchunk2Size
            accessWave.seek(40)
            accessWave.write(sizes, 4, 4)
        } catch (ex: IOException) {
            // Rethrow but we still close accessWave in our finally
            throw ex
        } finally {
            if (accessWave != null) {
                try {
                    accessWave.close()
                } catch (ex: IOException) {
                    //
                }
            }
        }
    }

    override fun stopRecord() {
//
        isRecording = false
    }

    companion object {
        const val TONE_LENGTH_MS = 150 // The length of DTMF tones in milliseconds
        const val DIAL_TONE_STREAM_TYPE = STREAM_DTMF
        const val TONE_RELATIVE_VOLUME =
            80 // The DTMF tone volume relative to other sounds in the stream

        private val sToneGeneratorLock = Any()

    }
}
