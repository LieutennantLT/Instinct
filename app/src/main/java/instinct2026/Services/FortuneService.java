package instinct2026.Services;

import java.util.Random;


public class FortuneService {

    public static FortuneResult generateFortune(){
        double winSeed = Math.round(Math.random() * 6);
        String winCase = Double.toString(winSeed);
        String winString;

        switch (winCase) {
                case "0.0":
                    winString = "You will WIN your next match!";
                    break;
                case "1.0":
                    winString = "You will BARELY WIN your next match!";
                    break;
                case "2.0":
                    winString = "You will TIE your next match.";
                    break;
                case "3.0":
                    winString = "You will BARELY LOSE your next match.";
                    break;
                case "4.0":
                    winString = "You will LOSE your next match.";
                    break;
                case "5.0":
                    winString = "You will score ZERO points.";
                    break;
                case "6.0":
                    winString = "You will BREAK THE HIGH SCORE!!!";
                    break;
                
                default:
                    winString = "the 8-Ball shattered :(";
                    break;
            }         
    
        double penaltySeed = Math.round(Math.random() * 3);
        String penaltyCase = Double.toString(penaltySeed);
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
                case "4.0":
                    penaltyString = "You will succesfully BRIBE the ref!";
                    break;
                case "5.0":
                    penaltyString = "You will recieve a WARNING.";
                    break;
                case "6.0":
                    penaltyString = "You will be KICKED OUT of the event!";
                    break;
                
                default:
                    penaltyString = "the 8-Ball shattered :(";
                    break;
            }
        
        double hpSeed = Math.round(Math.random() * 8);
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
                case "3.0":
                    hpString = "Your human player will perform AVERAGE.";
                    break;
                case "4.0":
                    hpString = "Your human player will perform TERRIBLY!";
                    break;
                case "5.0":
                    hpString = "Your human player will be ARRESTED mid match for tax evasion.";
                    break;
                case "6.0":
                    hpString = "Your human player will be ASSASINATAED by the CIA.";
                    break;
                case "7.0":
                    hpString = "Your human player will get RUN OVER by a rogue robot.";
                    break;
                case "8.0":
                    hpString = "Your human player will be DRAFTED to the NBA mid-match.";
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
