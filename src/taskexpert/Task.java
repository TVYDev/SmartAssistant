/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskexpert;

/**
 *
 * @author TVY
 */
public class Task {
    int taID;
        String taCreateDate, taTitle, taNote, taTaskDate, taCategory, taTime, taSound, taOpen, taBlock;
        int taTimeBlock;
        boolean taDoneStatus;
        
        Task(int id,String createD,String title,String noteTa,String cate,String soundTa,String taskD,String timeTa,
                String openTa,String blockTa,int timeBlock,boolean status){
            taID = id;          taCreateDate = createD;     taTitle = title;        taNote = noteTa;
            taCategory = cate;  taSound = soundTa;          taTaskDate = taskD;     taTime = timeTa;
            taOpen = openTa;    taBlock = blockTa;          taTimeBlock = timeBlock;taDoneStatus = status;
            
        }
}
