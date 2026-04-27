package instinct2026.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MatchSimulator {

    Random rand = new Random();

    public static class Result {
        public double redEPA;
        public double blueEPA;
        public double redWinProb;
        public final ArrayList<Double> redScores;
        public final ArrayList<Double> blueScores;

        public Result(double redEPA, double blueEPA, double redWinProb, ArrayList<Double> redScores, ArrayList<Double> blueScores) {
            this.redEPA = redEPA;
            this.blueEPA = blueEPA;
            this.redWinProb = redWinProb;
            this.redScores = redScores;
            this.blueScores = blueScores;
        }
    }

    public Result simulate(double r1, double r2, double r3,
                           double b1, double b2, double b3, int trials) {

        double redEPA = r1 + r2 + r3;
        double blueEPA = b1 + b2 + b3;

        ArrayList<Double> redScores = new ArrayList<>();
        ArrayList<Double> blueScores = new ArrayList<>();

        double redWins = 0;
        double standardDev = 0.15;

        for(int i = 0; i < trials; i++){
            double redScore = redEPA + (rand.nextGaussian() * (standardDev * redEPA));
            double blueScore = blueEPA + (rand.nextGaussian() * (standardDev * blueEPA));

            redScores.add(redScore);
            blueScores.add(blueScore);

            if(redScore > blueScore){
                redWins++;
            }
        }

        double redWinProb = (Math.round(((100*redWins)/trials) * 10))/10.0;

        return new Result(redEPA, blueEPA, redWinProb, redScores, blueScores);
    }
}