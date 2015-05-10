package com.medical.parakeet.parakeet;

import com.interaxon.libmuse.Eeg;

import java.util.ArrayList;

/**
 * Created by Senisa on 10/05/2015.
 */
public class EEG_Obj {

    public ArrayList<Double> prevTP9 = new ArrayList<Double>();
    public ArrayList<Double> prevFP1 = new ArrayList<Double>();
    public ArrayList<Double> prevFP2 = new ArrayList<Double>();
    public ArrayList<Double> prevTP10 = new ArrayList<Double>();

    public Double avgTP9 = -1.0;
    public Double avgFP1 = -1.0;
    public Double avgFP2 = -1.0;
    public Double avgTP10 = -1.0;

    public int val = 0;

    public boolean distressAlert = false;

    public void update(ArrayList<Double> data) {

        Double TP9 = data.get(Eeg.TP9.ordinal());
        Double FP1 = data.get(Eeg.FP1.ordinal());
        Double FP2 = data.get(Eeg.FP2.ordinal());
        Double TP10 = data.get(Eeg.TP10.ordinal());

        // IF IT'S 1.5 STDS ABOVE AVERAGA on TWO CHANNELS, SOUND THE ALARM!!
        if (prevFP1.size() > 20) {
            if (((Math.abs(TP9 - avgTP9) > val) && (Math.abs(FP1 - avgFP1) > val))) {
                distressAlert = true;
            }else if (((Math.abs(TP9 - avgTP9) > val) && (Math.abs(FP2 - avgFP2) > val))) {
                distressAlert = true;
            }else if (((Math.abs(TP9 - avgTP9) > val) && (Math.abs(TP10 - avgTP10) > val))) {
                distressAlert = true;
            } else if (((Math.abs(FP1 - avgFP1) > val) && (Math.abs(FP2 - avgFP2) > val))) {
                distressAlert = true;
            } if (((Math.abs(FP1 - avgFP1) > val) && (Math.abs(TP10 - avgTP10) > val))) {
                distressAlert = true;
            } else if (((Math.abs(TP10 - avgTP10) > val) && (Math.abs(FP2 - avgFP2) > val))) {
                distressAlert = true;
            }
        }


        prevTP9.add(TP9); // New end of the array
        if (prevTP9.size() > 20) {
            prevTP9.remove(0); // Last in last out, shift array to the left
        }
        avgTP9 = calcAvg(prevTP9);

        prevFP1.add(FP1); // New end of the array
        if (prevFP1.size() > 20) {
            prevFP1.remove(0); // Last in last out, shift array to the left
        }
        avgFP1 = calcAvg(prevFP1);

        prevFP2.add(FP2); // New end of the array
        if (prevFP2.size() > 20) {
            prevFP2.remove(0); // Last in last out, shift array to the left
        }
        avgFP2 = calcAvg(prevFP2);

        prevTP10.add(TP10); // New end of the array
        if (prevTP10.size() > 20) {
            prevTP10.remove(0); // Last in last out, shift array to the left
        }
        avgTP10 = calcAvg(prevTP10);
    }

    private double calcAvg(ArrayList <Double> marks) {
        Double sum = 0.0;
        if(!marks.isEmpty()) {
            for (Double mark : marks) {
                sum += mark;
            }
            return sum / marks.size();
        }
        return sum;
    }

    double getMean(ArrayList<Double> data)
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/data.size();
    }

    double getVariance(ArrayList<Double> data)
    {
        double mean = getMean(data);
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/data.size();
    }

    double getStdDev(ArrayList<Double> data)
    {
        return Math.sqrt(getVariance(data));
    }
}
