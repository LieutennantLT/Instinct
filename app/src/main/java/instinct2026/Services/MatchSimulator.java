package instinct2026.Services;

import java.util.Random;

public class MatchSimulator {

    Random rand = new Random();

    public static class Result {
        public double redEPA;
        public double blueEPA;
        public double redWinProb;

        public Result(double redEPA, double blueEPA, double redWinProb) {
            this.redEPA = redEPA;
            this.blueEPA = blueEPA;
            this.redWinProb = redWinProb;
        }
    }

    public Result simulate(double r1, double r2, double r3,
                           double b1, double b2, double b3, int trials) {

        double redEPA = r1 + r2 + r3;
        double blueEPA = b1 + b2 + b3;

        double redWins = 0;
        double standardDev = 0.05 * (redEPA + blueEPA);

        for(int i = 0; i < trials; i++){
            double redScore = redEPA + (rand.nextGaussian() * standardDev);
            double blueScore = blueEPA + (rand.nextGaussian() * standardDev);

            if(redScore > blueScore){
                redWins++;
            }
        }

        double redWinProb = (Math.round(((100*redWins)/trials) * 10))/10.0;

        return new Result(redEPA, blueEPA, redWinProb);
    }
}