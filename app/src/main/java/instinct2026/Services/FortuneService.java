package instinct2026.Services;

import java.util.Random;


public class FortuneService {

    public static FortuneResult generateFortune(){
        double winSeed = Math.round(Math.random() * 2);
        String winCase = Double.toString(winSeed);
        String winString;

        switch (winCase) {
                case "0.0":
                    winString = "You will WIN your next match! :D";
                    break;
                case "1.0":
                    winString = "You will BARELY WIN your next match! :)";
                    break;
                case "2.0":
                    winString = "You will TIE your next match. :/";
                    break;
                
                default:
                    winString = "the 8-Ball shattered :(";
                    break;
            }         
    
        double penaltySeed = Math.round(Math.random() * 3);
        String penaltyCase = Double.toString(winSeed);
        String penaltyString;

        switch (penaltyCase) {
                case "0.0":
                    penaltyString = "You will get a RED card.";
                    break;
                case "1.0":
                    penaltyString = "You will recieve ZERO penalties!";
                    break;
                case "2.0":
                    penaltyString = "You will recieve ZERO penalties!";
                    break;
                case "3.0":
                    penaltyString = "You will recieve a YELLOW card.";
                    break;
                
                default:
                    penaltyString = "the 8-Ball shattered :(";
                    break;
            }
        
        double hpSeed = Math.round(Math.random() * 2);
        String hpCase = Double.toString(winSeed);
        String hpString;

        switch (hpCase) {
                case "0.0":
                    hpString = "Your human player will FALL ASLEEP mid-match.";
                    break;
                case "1.0":
                    hpString = "Your human player will MISS every single shot.";
                    break;
                case "2.0":
                    hpString = "Your human player will perform GREAT!";
                    break;
                
                default:
                    hpString = "the 8-Ball shattered :(";
                    break;
            }

        return new FortuneResult(winString, penaltyString, hpString);

    }

    public static class FortuneResult {

        public String win;
        public String penalty;
        public String hP;

        public FortuneResult(String win, String penalty, String hP) {

            this.win = win;
            this.penalty = penalty;
            this.hP = hP;

        }
    }
    
}
