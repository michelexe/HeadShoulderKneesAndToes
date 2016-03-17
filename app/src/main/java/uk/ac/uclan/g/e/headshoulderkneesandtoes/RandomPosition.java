package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import java.util.Random;

/**
 * Created by ENZO on 17/03/2016.
 */
public class RandomPosition {
    private final PositionBand allFlagPostion []=
            {
            PositionBand.getHeadPosition(),
            PositionBand.getShoulderPosition(),
            PositionBand.getKneePosition(),
            PositionBand.getToesPosition()
            }; // all position

    private final int color []={
            R.color.ironBlue,
            R.color.violine,
            R.color.malachiteGreen,
            R.color.ocreRed

    }; /*
    each color match a position band,
    * blue for head
    * purple for shoulder
    *green for knees
    *orange for toes
    */

    private int colorDisplayed;

    private  PositionBand lastRandomResult;
        // the last result of the function getRandPosition
        // the point is not to have the same position twice

    /**
     * consctructor
     */
    public RandomPosition(){
        lastRandomResult = null;
        colorDisplayed = 0;
    }


    /**
     * get a random int
     * @return int
     */
    private int getRandomInt(){
        return (new Random().nextInt(allFlagPostion.length));
    }


    /**
     *return a random position diferent of
     * lastRandomResult
     * @return PositionBand
     */
    public PositionBand getRandPosition(){
        int random;
        PositionBand positionBand;
        do {
            random =getRandomInt();
            positionBand = allFlagPostion[random];
        }while (positionBand == lastRandomResult);

        colorDisplayed = color[random];
        lastRandomResult = positionBand;
        return positionBand;
    }

    /**
     *
     * @return
     */
    public int getColorDisplayed(){
        return colorDisplayed;
    }

}
