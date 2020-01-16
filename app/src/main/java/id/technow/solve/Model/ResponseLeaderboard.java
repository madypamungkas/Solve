package id.technow.solve.Model;

import java.util.ArrayList;

public class ResponseLeaderboard {
    private String status;


    private ArrayList<LeaderboarModel> result;

    public ResponseLeaderboard(String status, ArrayList<LeaderboarModel> result) {
        this.status = status;
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<LeaderboarModel> getResult() {
        return result;
    }
}
