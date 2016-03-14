package uk.ac.uclan.g.e.headshoulderkneesandtoes;

import com.microsoft.band.sensors.BandAccelerometerEvent;

/**
 * Created by ENZO on 09/03/2016.
 * @see BandAccelerometerEvent
 */
public class PositionBand {
    private PositionFlag flag =null;
    private float coordinateX,coordinateY,coordinateZ; // X,Y Z coordinates
    private static final float  // need to complete

        /*cordinates which given the right
        position of band to recognize the head
         */
                                X_HEAD = (float)-0.26375,
                                    HEAD_MARGIN_ERROR_ON_X = (float)0.067272762,   //
                                Y_HEAD = (float)0.17875,
                                    HEAD_MARGIN_ERROR_ON_Y=(float)0.03961262,  //0.047054991
                                Z_HEAD = (float)-0.92,
                                    HEAD_MARGIN_ERROR_ON_Z=(float)0.039457948,  //0.04687126


        /* coordinates which given the right
        position of band to recognize a shoulder
         */
                                X_SHOULDER = (float)-0.68875,
                                    SHOULDER_MARGIN_ERROR_ON_X=(float)0.123497377,
                                Y_SHOULDER = (float)-0.19875,
                                    SHOULDER_MARGIN_ERROR_ON_Y=(float)0.09011455,
                                Z_SHOULDER = (float)-0.6075,
                                    SHOULDER_MARGIN_ERROR_ON_Z=(float)0.187220722,


        /* coordinates which given the right
        position of band to recognize a knee
         */
                                X_KNEE = (float)0.957428571,
                                    KNEE_MARGIN_ERROR_ON_X=(float)0.235626727,
                                Y_KNEE = (float)0.199285714,
                                    KNEE_MARGIN_ERROR_ON_Y=(float)0.091485842,
                                Z_KNEE = (float)0.171571429,
                                    KNEE_MARGIN_ERROR_ON_Z=(float)0.080998523,

        /* coordinates which given the right
        position of band to recognize toes
        */
                                X_TOES = 0,
                                    TOES_MARGIN_ERROR_ON_X=(float)0,
                                Y_TOES = 0,
                                    TOES_MARGIN_ERROR_ON_Y=(float)0,
                                Z_TOES = 0,
                                    TOES_MARGIN_ERROR_ON_Z=(float)0;



    /**
     * constructor
     * @param event
     */
    public PositionBand(BandAccelerometerEvent event){
        this.coordinateX = event.getAccelerationX();
        this.coordinateY = event.getAccelerationY();
        this.coordinateZ= event.getAccelerationZ();
    }

    /**
     * constructor
     * from coordinate
     * @param x
     * @param y
     * @param z
     * @param flag
     */
    public PositionBand(float x, float y, float z,PositionFlag flag){
        this.coordinateX = x;
        this.coordinateY = y;
        this.coordinateZ = z;
        this.flag = flag;
    }

    /**
     * return the right band position
     * to recognize the head
     * @return PositionBand
     */
    public static PositionBand getHeadPosition(){
        return  new PositionBand(X_HEAD,Y_HEAD,Z_HEAD,PositionFlag.HEAD);
    }

    /**
     * return the right band position
     * to recognize a shoulder
     * @return PositionBand
     */
    public static PositionBand getShoulderPosition(){
        return new PositionBand(X_SHOULDER,Y_SHOULDER,Z_SHOULDER,PositionFlag.SHOULDER);
    }

    /**
     * return the right band position
     * to recognize a knee
     * @return PositionBand
     */
    public static PositionBand getKneePosition(){
        return new PositionBand(X_KNEE,Y_KNEE,Z_KNEE,PositionFlag.KNEE);
    }

    /**
     * return the right band position
     * to recognize toes
     * @return PositionBand
     */
    public static PositionBand getToesPosition(){
        return new PositionBand(X_TOES,Y_TOES,Z_TOES,PositionFlag.TOES);
    }


    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if(obj.getClass() == this.getClass()){
            PositionBand positionBand = (PositionBand)obj;
            PositionFlag positionFlag = positionBand.getFlag();
            if(positionFlag == PositionFlag.HEAD){ // if it is special postion :HEAD
                result= (coordinateX>=X_HEAD-HEAD_MARGIN_ERROR_ON_X)&&
                        (coordinateX<=X_HEAD+HEAD_MARGIN_ERROR_ON_X)&&
                        (coordinateY>=Y_HEAD-HEAD_MARGIN_ERROR_ON_Y)&&
                        (coordinateY<=Y_HEAD+HEAD_MARGIN_ERROR_ON_Y)&&
                        (coordinateZ>=Z_HEAD-HEAD_MARGIN_ERROR_ON_Z)&&
                        (coordinateZ<=Z_HEAD+HEAD_MARGIN_ERROR_ON_Z);
                /*
                if X is in [X_HEAD - MARGIN_ERROR; X_HEAD + MARGIN_ERROR]
                same for Y and Y
                 */
            }else if(positionFlag ==PositionFlag.SHOULDER){// SHOULDER
                result= (coordinateX>=X_SHOULDER-SHOULDER_MARGIN_ERROR_ON_X)&&
                        (coordinateX<=X_SHOULDER+SHOULDER_MARGIN_ERROR_ON_X)&&
                        (coordinateY>=Y_SHOULDER-SHOULDER_MARGIN_ERROR_ON_Y)&&
                        (coordinateY<=Y_SHOULDER+SHOULDER_MARGIN_ERROR_ON_Y)&&
                        (coordinateZ>=Z_SHOULDER-SHOULDER_MARGIN_ERROR_ON_Z)&&
                        (coordinateZ<=Z_SHOULDER+SHOULDER_MARGIN_ERROR_ON_Z);
                
                
            }else if(positionFlag == PositionFlag.KNEE){// KNEE
                result= (coordinateX>=X_KNEE-KNEE_MARGIN_ERROR_ON_X)&&
                        (coordinateX<=X_KNEE+KNEE_MARGIN_ERROR_ON_X)&&
                        (coordinateY>=Y_KNEE-KNEE_MARGIN_ERROR_ON_Y)&&
                        (coordinateY<=Y_KNEE+KNEE_MARGIN_ERROR_ON_Y)&&
                        (coordinateZ>=Z_KNEE-KNEE_MARGIN_ERROR_ON_Z)&&
                        (coordinateZ<=Z_KNEE+KNEE_MARGIN_ERROR_ON_Z);

            }else if(positionFlag==PositionFlag.TOES){// TOES
                result= (coordinateX>=X_TOES-TOES_MARGIN_ERROR_ON_X)&&
                        (coordinateX<=X_TOES+TOES_MARGIN_ERROR_ON_X)&&
                        (coordinateY>=Y_TOES-TOES_MARGIN_ERROR_ON_Y)&&
                        (coordinateY<=Y_TOES+TOES_MARGIN_ERROR_ON_Y)&&
                        (coordinateZ>=Z_TOES-TOES_MARGIN_ERROR_ON_Z)&&
                        (coordinateZ<=Z_TOES+TOES_MARGIN_ERROR_ON_Z);


            }else if(positionFlag == null){// a other position
                result=coordinateX==positionBand.coordinateX &&
                        coordinateY==positionBand.coordinateY&&
                        coordinateZ==positionBand.coordinateZ;
            }
        }

        return result;
    }

    // GETTER

    public float getCoordinateX() {
        return coordinateX;
    }

    public float getCoordinateY() {
        return coordinateY;
    }

    public float getCoordinateZ() {
        return coordinateZ;
    }

    public float getHeadMarginErrorOnX(){
        return HEAD_MARGIN_ERROR_ON_X;
    }

    public PositionFlag getFlag(){
        return this.flag;
    }
}
