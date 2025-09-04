import {Action} from '@ngrx/store';
import * as AuthActions from './actions';

const initialState = {
    isLoggedIn: !!sessionStorage.getItem('dfc-token'),
    token: sessionStorage.getItem('dfc-token'),
    currentUser: {
        email: 'mail@example.com',
        picture: null
    }
};

export function authReducer(
    state = initialState,
    action: AuthActions.LoginUser
) {
    switch (action.type) {
        case AuthActions.LOGIN_USER:
            sessionStorage.setItem('dfc-token', action.payload);
            return {
                ...state,
                isLoggedIn: true,
                token: action.payload
            };
            break;
        case AuthActions.LOGOUT_USER:
            break;
        case AuthActions.LOAD_USER:
            break;
        default:
            return state;
    }
}
