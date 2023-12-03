// Copyright 2023 Oğuzhan Topaloğlu
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


package com.twistral.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;


public class TSound {
    
    private Sound sound;
    private boolean isRunning;
    
    public TSound(Sound sound){
        this.sound = sound;
        isRunning = false;
    }
    
    public void play(float waitingTimeInSeconds){
        if(isRunning) return;
        this.sound.play();
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {@Override public void run() { isRunning = false; }}, waitingTimeInSeconds);
    }

    public void play(float waitingTimeInSeconds, float volume){
        if(isRunning) return;
        this.sound.play(volume);
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {@Override public void run() { isRunning = false; }}, waitingTimeInSeconds);
    }

    public void play(float waitingTimeInSeconds, float volume, float pitch, float pan){
        if(isRunning) return;
        this.sound.play(volume, pitch, pan);
        this.isRunning = true;
        Timer.schedule(new Timer.Task() {@Override public void run() { isRunning = false; }}, waitingTimeInSeconds);
    }


    public Sound getSound() { return sound; }
    public boolean isRunning() { return isRunning; }


}
