package itec.routeapp.utils;

import itec.routeapp.AppState;

/**
 * Created by Mihaela Ilin on 1/4/2017.
 */

public class AuthUtils {

    public static void signOut(){
        AppState state = AppState.get();
        state.getAuth().signOut();
//        state.setLatestUserId(state.getUserId());
        state.setUserId(null);
        //todo
//        state.clearState();
    }

}
