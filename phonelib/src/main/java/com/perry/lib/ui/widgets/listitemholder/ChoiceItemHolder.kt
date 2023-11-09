package com.perry.lib.ui.widgets.listitemholder

import com.perry.lib.R
import com.perry.lib.databinding.ListItemBinding
import com.perry.lib.util.getAttrColor

class ChoiceItemHolder(binding: ListItemBinding) : ListItemHolder(binding) {
    val dimenSpacingSmall by lazy { context.resources.getDimensionPixelSize(R.dimen.default_spacing_small) }
    private val _defaultBackgroundColor: Int
    private val _defaultTextColor: Int

    init {
        isImageVisible = false

        _defaultTextColor = context.getAttrColor(R.attr.colorOnSurfaceVariant)
        _defaultBackgroundColor = context.getAttrColor(R.attr.colorSurfaceVariant)

        binding.root.setPadding(dimenSpacing, dimenSpacingSmall, dimenSpacing, dimenSpacingSmall)
        binding.listItemTitle.setTextColor(_defaultTextColor)
        binding.listItemMainLayout.setCardBackgroundColor(_defaultBackgroundColor)
        binding.listItemMainConstraintLayout.setPadding(
            dimenSpacing,
            dimenSpacing,
            0,
            dimenSpacing
        )
    }

    fun setSelected(isSelected: Boolean) {
        binding.listItemMainLayout.setCardBackgroundColor(if (isSelected) context.getAttrColor(R.attr.colorSecondaryContainer) else _defaultBackgroundColor)
        binding.listItemTitle.setTextColor(if (isSelected) context.getAttrColor(R.attr.colorOnSecondaryContainer) else _defaultTextColor)
    }
}