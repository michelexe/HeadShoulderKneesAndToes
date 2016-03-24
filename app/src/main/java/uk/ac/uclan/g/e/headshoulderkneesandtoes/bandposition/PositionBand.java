package uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition;

import com.microsoft.band.sensors.BandAccelerometerEvent;

/**
 * Created by ENZO on 09/03/2016.
 * @see BandAccelerometerEvent
 */
public class PositionBand {
    private PositionFlag flag =null;
    private float coordinateX,coordinateY,coordinateZ; // X,Y Z coordinates
    private static float  // need to complete

        /*cordinates which given the right
        position of band to recognize the head
         */
                                X_HEAD = (float)-0.26375,
                                    HEAD_MARGIN_ERROR_ON_X = (float)0.25, //0.079911887,   //
                                Y_HEAD = (float)0.17875,
                                    HEAD_MARGIN_ERROR_ON_Y=(float)0.25,//0.047054991,  //0.047054991
                                Z_HEAD = (float)-0.92,
                                    HEAD_MARGIN_ERROR_ON_Z=(float)0.25,//0.04687126,  //0.04687126


        /* coordinates which given the right
        position of band to recognize a shoulder
         */
                                X_SHOULDER = (float)-0.68875,
                                    SHOULDER_MARGIN_ERROR_ON_X=(float)0.25, //0.123497377
                                Y_SHOULDER = (float)-0.19875,
                                    SHOULDER_MARGIN_ERROR_ON_Y=(float)0.25,//0.09011455
                                Z_SHOULDER = (float)-0.6075,
                                    SHOULDER_MARGIN_ERROR_ON_Z=(float)0.187220722,


        /* coordinates which given the right
        position of band to recognize a knee
         */
                                X_KNEE = (float)0.9565,
                                    KNEE_MARGIN_ERROR_ON_X=(float)0.15,//0.022355495
                                Y_KNEE = (float)0.198625,
                                    KNEE_MARGIN_ERROR_ON_Y=(float)0.15,//0.077377297
                                Z_KNEE = (float)0.174,
                                    KNEE_MARGIN_ERROR_ON_Z=(float)0.15, //0.069380436

        /* coordinates which given the right
        position of band to recognize toes
        */
                                X_TOES = (float)0.97775,
                                    TOES_MARGIN_ERROR_ON_X=(float)0.15,  //0.017835428
                                Y_TOES = (float)-0.064,
                                    TOES_MARGIN_ERROR_ON_Y=(float)0.15,  //0.085425988
                                Z_TOES = (float)0.057125,
                                    TOES_MARGIN_ERROR_ON_Z=(float)0.15; //0.092473424



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

    public static void setxHead(float xHead) {
        X_HEAD = xHead;
    }

    public static void setyHead(float yHead) {
        Y_HEAD = yHead;
    }

    public static void setzHead(float zHead) {
        Z_HEAD = zHead;
    }

    public static void setxShoulder(float xShoulder) {
        X_SHOULDER = xShoulder;
    }

    public static void setyShoulder(float yShoulder) {
        Y_SHOULDER = yShoulder;
    }

    public static void setzShoulder(float zShoulder) {
        Z_SHOULDER = zShoulder;
    }

    public static void setxKnee(float xKnee) {
        X_KNEE = xKnee;
    }

    public static void setyKnee(float yKnee) {
        Y_KNEE = yKnee;
    }

    public static void setzKnee(float zKnee) {
        Z_KNEE = zKnee;
    }

    public static void setxToes(float xToes) {
        X_TOES = xToes;
    }

    public static void setyToes(float yToes) {
        Y_TOES = yToes;
    }

    public static void setzToes(float zToes) {
        Z_TOES = zToes;
    }
}
