package com.perry.lib.ui.widgets.listitemholder

import android.content.res.ColorStateList
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.perry.lib.R
import com.perry.lib.databinding.ListItemBinding
import com.perry.lib.util.getAttrColor

class MenuItemHolder(binding: ListItemBinding) : ListItemHolder(binding) {

    init {
        setImageSize(context.resources.getDimensionPixelSize(R.dimen.image_size_small))
        binding.listItemImage.imageTintList =
            ColorStateList.valueOf(context.getAttrColor(R.attr.colorOnSurface))
        binding.listItemTitle.typeface =
            ResourcesCompat.getFont(context, R.font.google_sans_regular)
        binding.listItemTitle.setTextAppearance(R.style.Chooloo_Text_Title)
        binding.listItemImage.layoutParams =
            ConstraintLayout.LayoutParams(binding.listItemImage.layoutParams as ConstraintLayout.LayoutParams)
                .apply {
                    marginStart = dimenSpacing
                }
        binding.listItemImage.setPadding(0, 3, 3, 3)
    }
}