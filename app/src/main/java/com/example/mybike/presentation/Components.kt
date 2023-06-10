package com.example.mybike.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.window.Dialog
import com.example.mybike.R
import com.example.mybike.ui.theme.Black
import com.example.mybike.ui.theme.Blue
import com.example.mybike.ui.theme.BlueWithAlpha
import com.example.mybike.ui.theme.DarkBlue
import com.example.mybike.ui.theme.Gray
import com.example.mybike.ui.theme.LightBlue
import com.example.mybike.ui.theme.Typography
import com.example.mybike.ui.theme.White
import com.example.mybike.ui.theme.WhiteWithAlpha
import com.example.mybike.vo.ThreeDotsDropdownItem

private const val NONE = "None"

@Composable
fun CustomButton(modifier: Modifier = Modifier, text: String, enabled: Boolean, onClick: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Blue, contentColor = White, disabledContainerColor = BlueWithAlpha, disabledContentColor = WhiteWithAlpha),
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.d4))
    ) {
        Text(text = text, textAlign = TextAlign.Center, style = Typography.displayMedium)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    customNavigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    backgroundColor: Color = Black
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title, style = Typography.titleMedium, color = White) },
        navigationIcon = { customNavigationIcon() },
        actions = { actions() },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = backgroundColor)
    )
}

@Composable
fun TopBarAddAction(onAddBikeClicked: () -> Unit, text: String) {
    Row(modifier = Modifier
        .clickable { onAddBikeClicked() }
        .padding(end = dimensionResource(id = R.dimen.d12))
    ) {
        Icon(imageVector = Icons.Default.Add, tint = White, contentDescription = null)
        Text(text = text, color = White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDropDown(elements: List<String>, CustomLabel: @Composable () -> Unit = {}, modifier: Modifier = Modifier, selectedItem: String = NONE, onSelectedItem: (value: String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember(selectedItem) { mutableStateOf(selectedItem.ifBlank { NONE }) }

    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                CustomLabel()
                Column(verticalArrangement = Arrangement.Center) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { CustomTrailingIcon(expanded = expanded, color = White) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = White,
                            unfocusedTextColor = White,
                            focusedContainerColor = DarkBlue,
                            unfocusedContainerColor = DarkBlue,
                            disabledContainerColor = DarkBlue,
                            focusedBorderColor = Gray,
                            disabledBorderColor = Gray,
                            unfocusedBorderColor = Gray
                        ), textStyle = Typography.displayMedium
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(color = DarkBlue)
                            .exposedDropdownSize()
                    ) {
                        elements.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedText = item
                                    onSelectedItem(item)
                                    expanded = false
                                },
                                colors = MenuDefaults.itemColors(textColor = White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = DarkBlue)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomLabel(text: String, @DrawableRes iconId: Int?, textColor: Color, iconTint: Color?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, style = Typography.displaySmall, color = Gray)
        iconId?.let { it -> Icon(painter = painterResource(id = it), contentDescription = null, tint = iconTint ?: Gray) }
    }
}

@ExperimentalMaterial3Api
@Composable
fun CustomTrailingIcon(expanded: Boolean, color: Color) {
    Icon(
        Icons.Filled.ArrowDropDown,
        null,
        Modifier.rotate(if (expanded) 180f else 0f),
        tint = color
    )
}

@Composable
fun CustomThreeDotsDropdown(modifier: Modifier = Modifier, elements: List<ThreeDotsDropdownItem>) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.more),
                tint = White
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(LightBlue)
        ) {
            elements.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = CenterVertically) {
                            Icon(painter = painterResource(id = item.icon), contentDescription = null, tint = item.iconTint)
                            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.d12)))
                            Text(item.text)
                        }
                    },
                    onClick = { item.onClick() },
                    colors = MenuDefaults.itemColors(textColor = item.textColor),
                )
            }
        }
    }
}

@Composable
fun CustomProgressBar(progress: Float, modifier: Modifier = Modifier) {
    Box(contentAlignment = CenterStart, modifier = modifier) {
        Row(verticalAlignment = CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.loading_circle), contentDescription = null, tint = Blue)
            Icon(painter = painterResource(id = R.drawable.loading_bar), contentDescription = null, tint = LightBlue)
            Icon(painter = painterResource(id = R.drawable.loading_bolt), contentDescription = null, tint = White)
        }
        Row(verticalAlignment = CenterVertically) {
            Icon(painter = painterResource(id = R.drawable.loading_over), contentDescription = null, tint = Blue, modifier = Modifier.fillMaxWidth(progress))
            Icon(painter = painterResource(id = R.drawable.loading_wrench), contentDescription = null, tint = Blue)
        }
    }
}

@Preview
@Composable
fun PreviewCustomProgressBar() {
    CustomProgressBar(0.4f)
}

@Preview
@Composable
fun PreviewCustomButton() {
    CustomButton(
        text = "Button", enabled = true, onClick = {}, modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.d12))
    )
}

@Preview
@Composable
fun PreviewCustomTopBar() {
    CustomTopBar(
        title = "Add Bike",
        customNavigationIcon = {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, tint = White)
        },
        actions = {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = White)
            Text(text = "Add", color = White, style = Typography.displayLarge, modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.d8)))
        }
    )
}

@Preview
@Composable
fun PreviewCustomDropdown() {
    Column(modifier = Modifier.fillMaxHeight()) {
        CustomDropDown(elements = listOf("Road Bike", "MTB", "Electric", "Hybrid"), modifier = Modifier.height(dimensionResource(id = R.dimen.d60)), onSelectedItem = {})
    }
}

@Preview(backgroundColor = 0xFF222C48)
@Composable
fun PreviewThreeDotsCustomDropdown() {
    CustomThreeDotsDropdown(
        elements = listOf(
            ThreeDotsDropdownItem("Edit", R.drawable.icon_edit, White, {}, White, Typography.titleSmall),
            ThreeDotsDropdownItem("Delete", R.drawable.icon_delete, White, {}, White, Typography.titleSmall)
        )
    )
}