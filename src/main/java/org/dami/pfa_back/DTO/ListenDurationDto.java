package org.dami.pfa_back.DTO;

public class ListenDurationDto {
     private int durationListenedSeconds;

     public int getDurationListenedSeconds() {
          return durationListenedSeconds;
     }

     public ListenDurationDto setDurationListenedSeconds(int durationListenedSeconds) {
          this.durationListenedSeconds = durationListenedSeconds;
          return this;
     }
}
