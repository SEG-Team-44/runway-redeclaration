package com.team44.runwayredeclarationapp.controller;
import com.team44.runwayredeclarationapp.model.Aircraft;
import com.team44.runwayredeclarationapp.model.Airport;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.Runway;
import java.util.List;

public class Calculator {

  public static double calcLandingTowardsObs(Airport airport, Aircraft aircraft,Obstacles obstacle){
    List<Runway> rws = airport.getRunways();
    double pos = obstacle.getPositionR();
    double resa = rws.get(1).getResa();
    double finalLDA;

    finalLDA = pos - resa - 60;

  return finalLDA;
  }

  public static double calcLandingAwayFromObs(Airport airport, Aircraft aircraft, Obstacles obstacle){
    List<Runway> rws = airport.getRunways();
    double h = obstacle.getHeight();
    double pos = obstacle.getPositionL();
    double bp = aircraft.getBlastProt();
    double lda = rws.get(1).getLDA1();
    double resa = rws.get(1).getResa();
    double finalLDA;

    if (resa < bp){
      if (bp < h * 50) {
        finalLDA = lda - pos - (h * 50) - 60;
      } else {
        finalLDA = lda - pos - bp;
      }
    } else {
      if (resa < h * 50) {
        finalLDA = lda - pos - (h * 50) - 60;
      } else {
        finalLDA = lda - pos - resa - 60;
      }

    }

    return finalLDA;
  }

  public static double calcTakeOffTowardsObs(Airport airport, Obstacles obstacle){

    return 1;
  }

  public static double calcTakeOffAwayFromObs(Airport airport, Obstacles obstacle){

    return 1;
  }
}
