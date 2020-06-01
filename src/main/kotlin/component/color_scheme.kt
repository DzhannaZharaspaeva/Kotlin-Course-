package component

import data.ColorScheme
import data.State
import data.colorschemeList
import hoc.withDisplayName
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLBodyElement
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.redux.rConnect
import redux.ChangeColorScheme
import redux.RAction
import redux.WrapperAction
import kotlin.browser.document

interface ColorSchemeListProps : RProps {
    var color_schemes: Array<ColorScheme>
    var selected_color_scheme: ColorScheme
    var onClick: (Event) -> Unit

}

val fcolorSchemeListContainer = functionalComponent<ColorSchemeListProps> {
    span(
    ) {
        attrs.onClickFunction = it.onClick
    }
}

/*fun RBuilder.colorSchemeList(
        color_scheme: ColorScheme,
        cssClass: String,
        onClick: (Event) -> Unit
) = child(
        withDisplayName("ColorSchemes", fcolorSchemeListContainer)
) {
    attrs.selected_color_scheme = color_scheme
    attrs.color_schemes = colorschemeList()
    attrs.onClick = onClick
}*/

interface colorSchemeStateProps : RProps {
    var color_scheme: ColorScheme  //выбранная цветовая схема
    var color_schemes: Array<ColorScheme> //список цветовых схем, которые доступны в пп
}

interface colorSchemeDispatchProps : RProps {
    var save: (ColorScheme) -> Unit //Unit для обработки выбора цветовой схемы
}


interface colorSchemeListProps : RProps {
    var color_schemes: Array<ColorScheme> //список цветовых схем, которые доступны в ПП
    var color_scheme: ColorScheme //выбранная цветовая схема
    var save: (ColorScheme) -> Unit //Unit для обработки выбора цветовой схемы
}

fun fcolorSchemeList(
) =
        functionalComponent<colorSchemeListProps> { props ->
            //получаем body и устанавливаем для него белый цвет
            //это необходимо так как в компоненте "control_date"
            //цвет body будет установлен в цвет выбранной схемы
            val root = document.getElementById("root") as HTMLDivElement
            val body = root.parentElement as HTMLBodyElement
            body.bgColor = "white"
            //заголовок
            h2 { +"Цветовая схема результатов контрольных недель" }
            //выпадающий список с цветовыми схемами
            
            select {
                props.color_schemes.mapIndexed { index, colorscheme ->
                    option {
                        attrs.value = "$index"
                        //метод toString() вернет название схемы
                        attrs.label = colorscheme.toString()
                        //если colorscheme выбрана текущей, установить ее выбранной
                        if(colorscheme.toString() == props.color_scheme.toString())
                            attrs.selected = true
                    }
                }
                //при изменении выбранного элемента вызываем props.save
                attrs.onChangeFunction = {
                    val i = it.target as HTMLSelectElement
                    props.save(colorschemeList()[i.value.toInt()])
                }
            }
        }


val colorSchemeListContainer =
        rConnect<
                State,
                RAction,
                WrapperAction,
                colorSchemeStateProps,
                colorSchemeStateProps,
                colorSchemeDispatchProps,
                colorSchemeListProps
                >(
                { state, _ ->
                    //получаем из состояния выбранную цветовую схему
                    color_scheme = state.color_scheme
                    //получаем список доступных цветовых схем
                    //(в состоянии хранить данный список не рационально)
                    color_schemes = colorschemeList()
                },
                { dispatch, ownProps ->
                    save = {
                        //вызываем метод dispatch для обработки события ChangeColorScheme()
                        dispatch(ChangeColorScheme(it))
                    }
                }
        )(//компонент
                withDisplayName(
                        "ControlDateList",
                        fcolorSchemeList()

                ).unsafeCast<RClass<colorSchemeListProps>>()
        )
