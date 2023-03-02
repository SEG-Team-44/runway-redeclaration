package com.team44.runwayredeclarationapp.utility;
import com.team44.runwayredeclarationapp.model.Aircraft;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.List;

public class Calculator {

  public static double calcLandingTowardsObs(Airport airport, Aircraft aircraft,Obstacles obstacle){

    return 1;
  }

  public static double calcLandingAwayFromObs(Airport airport, Aircraft aircraft, Obstacles obstacle){
    List<Runway> rws = airport.getRunways();
    double h = obstacle.getHeight();
    double pos = obstacle.getPosition();
    double bp = aircraft.getBlastProt();
    double lda = rws.get(1).getLDA1();
    double resa = rws.get(1).getResa();
    double finalValue;

    if (resa < bp){
      if (bp < h * 50) {
        finalValue = lda - pos - (h * 50) - 60;
      } else {
        finalValue = lda - pos - bp;
      }
    } else {
      if (resa < h * 50) {
        finalValue = lda - pos - (h * 50) - 60;
      } else {
        finalValue = lda - pos - resa - 60;
      }

    }

    return finalValue;
  }

  public static double calcTakeOffTowardsObs(Airport airport, Obstacles obstacle){

    return 1;
  }

  public static double calcTakeOffAwayFromObs(Airport airport, Obstacles obstacle){

    return 1;
  }
}
