package com.living.roomrental.comman;

import android.Manifest;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.living.roomrental.R;

import org.webrtc.AudioSource;
import org.webrtc.EglBase;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;

import java.util.ArrayList;
import java.util.List;

public class WebCallingActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private SurfaceView localVideoView;
    private SurfaceView remoteVideoView;
    private Button startCallButton;
    private Button endCallButton;

    private PeerConnectionFactory peerConnectionFactory;
    private PeerConnection peerConnection;
    private MediaStream localStream;
    private VideoSource videoSource;
    private AudioSource audioSource;
    private VideoCapturer videoCapturer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_calling);

        localVideoView = findViewById(R.id.localVideoView);
        remoteVideoView = findViewById(R.id.remoteVideoView);
        startCallButton = findViewById(R.id.startCallButton);
        endCallButton = findViewById(R.id.endCallButton);

        // Request camera and microphone permissions
        requestPermissions();

        // Initialize WebRTC
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(this)
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        // Create a PeerConnectionFactory
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .createPeerConnectionFactory();

        // Create and initialize local video and audio sources
        EglBase eglBase = EglBase.create();
        videoCapturer = createVideoCapturer(); // Implement this method to create a VideoCapturer
        videoSource = peerConnectionFactory.createVideoSource(videoCapturer.isScreencast());
        audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());

        // Create local stream
        localStream = peerConnectionFactory.createLocalMediaStream("localStream");
        localStream.addTrack(peerConnectionFactory.createVideoTrack("videoTrack", videoSource));
        localStream.addTrack(peerConnectionFactory.createAudioTrack("audioTrack", audioSource));

        //localVideoView.init(eglBase.getEglBaseContext(), null);

        // Set up peer connection configuration
        List<PeerConnection.IceServer> iceServers = new ArrayList<>();
        iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
// Add more ICE servers as needed

        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);

//        PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(
//                // Set your ICE servers here (STUN/TURN)
//        );

        // Create a peer connection

//        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, new CustomPeerConnectionObserver() {
//            // Implement necessary callbacks
//        });
    }
    private VideoCapturer createVideoCapturer() {
        // Implement this method to create and configure a VideoCapturer (e.g., Camera2Capturer).
        // Return the created VideoCapturer.
        return null; // Replace with your implementation
    }

    private void requestPermissions() {
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    public void startCall(View view) {
        // Implement logic to start the call
    }

    public void endCall(View view) {
        // Implement logic to end the call
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Handle permission results
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up resources
        if (peerConnection != null) {
            peerConnection.dispose();
            peerConnection = null;
        }
        if (peerConnectionFactory != null) {
            peerConnectionFactory.dispose();
            peerConnectionFactory = null;
        }
    }
}