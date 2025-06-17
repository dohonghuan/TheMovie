package com.example.w3wthemovie.android

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.w3wthemovie.android.ui.list.SearchView
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchViewTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val placeholderText = "Search movies"
    private val searchTextFieldTag = "search_text_field"
    private val clearIconButtonTag = "clear_icon_button"
    private val searchIconContentDescription = "Search Icon"

    @Test
    fun searchView_initialState_withValue_displaysValueAndClearButton() {
        val initialSearchValue = "Initial Query"
        var currentSearchValue = initialSearchValue

        composeTestRule.setContent {
            W3WTheMovieTheme {
                SearchView(
                    searchValue = currentSearchValue,
                    onValueChange = { newValue -> currentSearchValue = newValue }
                )
            }
        }

        // Verify TextField displays the initial search value
        composeTestRule.onNodeWithText(initialSearchValue).assertIsDisplayed()
        composeTestRule.onNodeWithTag(searchTextFieldTag).assertTextEquals(initialSearchValue)

        // Verify placeholder is NOT displayed
        composeTestRule.onNodeWithText(placeholderText).assertDoesNotExist()

        // Verify clear button is displayed
        composeTestRule.onNodeWithTag(clearIconButtonTag).assertIsDisplayed()

        // Verify search icon is displayed
        composeTestRule.onNodeWithContentDescription(searchIconContentDescription, useUnmergedTree = true)
            .assertIsDisplayed()
    }
}
