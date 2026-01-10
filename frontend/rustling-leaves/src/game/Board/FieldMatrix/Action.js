export const actions = {
    DRAW: "draw",
    TICK: "tick",
    CONFIRM: "confirm",
    WAIT: "wait"
}

export const events = {
    AREA_VALID: "AREA_VALID",
    AREA_INVALID: "AREA_INVALID",
    TYPE_VALID: "TYPE_VALID",
    TYPE_INVALID: "TYPE_INVALID",
    CONFIRMED: "CONFIRMED",
    NEW_ROUND_STARTED: 5
}

export function actionReducer(state, event) {
    if(event === events.NEW_ROUND_STARTED) {
        return actions.DRAW;
    }
    
    switch (state) {
        case actions.DRAW:
            if (event === events.AREA_VALID) 
                return actions.TICK;
            else    
                return actions.DRAW;

        case actions.TICK:
            if (event === events.TYPE_VALID) 
                return actions.CONFIRM;
            else if (event === events.AREA_INVALID) 
                return actions.DRAW;
            else
                return actions.TICK;

        case actions.CONFIRM:
            if (event === events.TYPE_VALID) 
                return actions.CONFIRM;
            else if(event === events.TYPE_INVALID || event === events.AREA_VALID)
                return actions.TICK
            else if (event === events.AREA_INVALID) 
                return actions.DRAW;
            else if (event === events.CONFIRMED) 
                return actions.WAIT;
            else
                return actions.CONFIRM;

        default:
            return state;
    }
}