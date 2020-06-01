package redux

import data.*

fun rootReducer_m(state: State, action: RAction): State{
    when (action) {
        is ChangeColorScheme -> {
            //меняем текущую цветовую схему
            state.color_scheme = action.newColorScheme
        }
        is ChangeControlDate -> {
            //state.control_date - Array<Date> из трех элементов даты начала контрольной недели
            //меняем необходимую дату 
            state.control_date[action.id] = action.newControlDate
        }
    }
    return state
}

