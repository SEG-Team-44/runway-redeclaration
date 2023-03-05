package com.team44.runwayredeclarationapp.controller;
import com.team44.runwayredeclarationapp.model.Aircraft;
import com.team44.runwayredeclarationapp.model.Obstacles;
import com.team44.runwayredeclarationapp.model.Runway;

public class Calculator {

  public static double calcLandingTowardsObs(Runway rw, Obstacles obstacle){
    double pos = obstacle.getPositionR();
    double resa = rw.getResa();
    double finalLDA;

    finalLDA = pos - resa - 60;

  return finalLDA;
  }

  public static double calcLandingAwayFromObs(Runway rw, Aircraft aircraft, Obstacles obstacle){
    double h = obstacle.getHeight();
    double pos = obstacle.getPositionL();
    double bp = aircraft.getBlastProt();
    double lda = rw.getLDA1();
    double resa = rw.getResa();
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

  public static Runway calcTakeOffTowardsObs(Runway rw, Obstacles obstacle){
    double h = obstacle.getHeight();
    double pos = obstacle.getPositionL();
    double resa = rw.getResa();

    double finalValue = pos - (h * 50) - resa;
    double[] newParams = new double[15];

    newParams[0] = rw.getRunwayL();
    newParams[1] = rw.getRunwayW();
    newParams[2] = rw.getStripL();
    newParams[3] = rw.getStripW();
    newParams[4] = rw.getClearwayW();
    newParams[5] = finalValue;
    newParams[6] = finalValue;
    newParams[7] = finalValue;
    newParams[8] = rw.getLDA1();
    newParams[9] = rw.getDisThresh1();
    newParams[10] = rw.getTORA2();
    newParams[11] = rw.getLDA1();
    newParams[12] = rw.getDisThresh1();
    newParams[13] = rw.getTORA2();
    newParams[14] = rw.getLDA1();
    newParams[15] = rw.getDisThresh1();

    rw.updateParameters(newParams);
    return rw;
  }

  public static Runway calcTakeOffAwayFromObs(Runway rw, Aircraft aircraft, Obstacles obstacle){
    double h = obstacle.getHeight();
    double pos = obstacle.getPositionL();
    double bp = aircraft.getBlastProt();
    double resa = rw.getResa();

    double finalValue = pos - (h * 50) - resa;
    double[] newParams = new double[15];

    newParams[0] = rw.getRunwayL();
    newParams[1] = rw.getRunwayW();
    newParams[2] = rw.getStripL();
    newParams[3] = rw.getStripW();
    newParams[4] = rw.getClearwayW();
    newParams[5] = finalValue;
    newParams[6] = finalValue;
    newParams[7] = finalValue;
    newParams[8] = rw.getLDA1();
    newParams[9] = rw.getDisThresh1();
    newParams[10] = rw.getTORA2();
    newParams[11] = rw.getLDA1();
    newParams[12] = rw.getDisThresh1();
    newParams[13] = rw.getTORA2();
    newParams[14] = rw.getLDA1();
    newParams[15] = rw.getDisThresh1();

    rw.updateParameters(newParams);
    return rw;
  }
}
