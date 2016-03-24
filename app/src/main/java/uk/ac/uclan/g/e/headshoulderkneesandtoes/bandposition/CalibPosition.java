package uk.ac.uclan.g.e.headshoulderkneesandtoes.bandposition;

import com.microsoft.band.sensors.BandAccelerometerEvent;

import java.util.ArrayList;

/**
 * Created by ENZO on 21/03/2016.
 * the point is stock all events in order to calibrate
 * a Microsoft band
 * @see BandAccelerometerEvent
 */
public class CalibPosition {
    private ArrayList<BandAccelerometerEvent> eventList;

    public CalibPosition(){
        eventList = new ArrayList<BandAccelerometerEvent>();
    }

    /**
     * add even to the array list
     * @param event
     */
    public void addEvent (BandAccelerometerEvent event){
        eventList.add(event);
    }

    /**
     * get the average of all X coordinate
     * @return avgX
     */
    private float avgAcceleratorX(){
        float avgX = 0;
        for (int i=0; i<eventList.size();i++){
            avgX+=eventList.get(i).getAccelerationX();
        }
        return avgX/eventList.size();
    }

    /**
     * get the average of all Y coordinate
     * @return avgY
     */
    private float avgAcceleratorY(){
        float avgY = 0;
        for (int i=0; i<eventList.size();i++){
            avgY+=eventList.get(i).getAccelerationY();
        }
        return avgY/eventList.size();
    }

    /**
     * get the average of all Z coordinate
     * @return avgZ
     */
    private float avgAcceleratorZ(){
        float avgZ = 0;
        for (int i=0; i<eventList.size();i++){
            avgZ+=eventList.get(i).getAccelerationZ();
        }
        return avgZ/eventList.size();
    }

    /**
     * get the min values of an array
     * @param coordinate
     * @return
     */
    private float getMin(float coordinate[]){
        float result;
        if(coordinate.length !=0){
            result=coordinate[0];
            for(int i=0;i<coordinate.length; i++){
                if(result <coordinate[i]){
                    result = coordinate[i];
                }
            }
        }else {
            result=0;
        }
        return result;
    }


    /**
     * get value Max of an array
     * @param coordinate
     * @return
     */
    private float getMax(float coordinate []){
        float result;
        if(coordinate.length !=0){
            result=coordinate[0];
            for(int i=0;i<coordinate.length; i++){
                if(result >coordinate[i]){
                    result = coordinate[i];
                }
            }
        }else {
            result=0;
        }
        return result;
    }


    /**
     *
     * @return
     */
    private float [] getArrayCoordinateX(){
        float []result = new float[eventList.size()];
        for(int i=0;i<eventList.size();i++){
            result[i]= eventList.get(i).getAccelerationX();
        }
        return result;
    }


    /**
     *
     * @return
     */
    private float []getArrayCoordinateY(){
        float []result = new float[eventList.size()];
        for(int i=0;i<eventList.size();i++){
            result[i]= eventList.get(i).getAccelerationY();
        }
        return result;
    }


    /**
     * get an Array
     * @return
     */
    private float []getArrayCoordinateZ(){
        float []result = new float[eventList.size()];
        for(int i=0;i<eventList.size();i++){
            result[i]= eventList.get(i).getAccelerationZ();
        }
        return result;
    }


    /**
     * set average to PostionBand
     * depends on the flag given
     *modify default values of coordinate X,Y, Z
     * from the class PositionBand
     * @param flag
     */
    public void setCalibCoordinate(PositionFlag flag){
        if(flag == PositionFlag.HEAD){// changes HEAD
            PositionBand.setxHead(avgAcceleratorX());
            PositionBand.setyHead(avgAcceleratorY());
            PositionBand.setzHead(avgAcceleratorZ());

        }else if(flag == PositionFlag.SHOULDER){
            PositionBand.setxShoulder(avgAcceleratorX());
            PositionBand.setyShoulder(avgAcceleratorY());
            PositionBand.setzShoulder(avgAcceleratorZ());

        }else if(flag == PositionFlag.KNEE){
            PositionBand.setxKnee(avgAcceleratorX());
            PositionBand.setyKnee(avgAcceleratorY());
            PositionBand.setzKnee(avgAcceleratorZ());

        }else if(flag == PositionFlag.TOES){
            PositionBand.setxToes(avgAcceleratorX());
            PositionBand.setyToes(avgAcceleratorY());
            PositionBand.setzToes(avgAcceleratorZ());
        }
    }

    /**
     * empty the eventList
     */
    public void emptyEventList(){
        for (int i=0;i<eventList.size();i++){
            eventList.remove(i);
        }
    }
}
