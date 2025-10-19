// IVisualizer.aidl
package com.example.aidlmodule;

// Declare any non-default types here with import statements

interface IVisualizer {
    /**
     * Updates the media session ID for visualization
     * @param sessionId The audio session ID from MediaPlayer
     */
    void updateMediaSessionId(int sessionId);
    void setFftProcessor(String name);
}
