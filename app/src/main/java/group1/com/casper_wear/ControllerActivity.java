package group1.com.casper_wear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;

public class ControllerActivity extends Activity {

    private TextView directionText;
    private RelativeLayout layout_joystick;
    JoyStick driveStick;

    // Image's for Joystick
    private ImageView image_joystick, image_border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                directionText = (TextView)stub.findViewById(R.id.direction);
                // Joystick
                layout_joystick = (RelativeLayout)stub.findViewById(R.id.layout_joystick);

                driveStick = new JoyStick(getApplicationContext()
                        , layout_joystick, R.drawable.image_button);
                driveStick.setStickSize(100, 100);
                driveStick.setLayoutSize(250, 250);
                driveStick.setLayoutAlpha(200);
                driveStick.setStickAlpha(100);
                driveStick.setOffset(50);
                driveStick.setMinimumDistance(50);


                layout_joystick.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View arg0, MotionEvent arg1) {
                        driveStick.drawStick(arg1);
                        if (arg1.getAction() == MotionEvent.ACTION_DOWN
                                || arg1.getAction() == MotionEvent.ACTION_MOVE) {
                            int direction = driveStick.get8Direction();
                            if (direction == JoyStick.STICK_UP) {
                                directionText.setText("Direction : Up");
                            } else if (direction == JoyStick.STICK_UPRIGHT) {
                                directionText.setText("Direction : Up Right");
                            } else if (direction == JoyStick.STICK_RIGHT) {
                                directionText.setText("Direction : Right");
                            } else if (direction == JoyStick.STICK_DOWNRIGHT) {
                                directionText.setText("Direction : Down Right");
                            } else if (direction == JoyStick.STICK_DOWN) {
                                directionText.setText("Direction : Down");
                            } else if (direction == JoyStick.STICK_DOWNLEFT) {
                                directionText.setText("Direction : Down Left");
                            } else if (direction == JoyStick.STICK_LEFT) {
                                directionText.setText("Direction : Left");
                            } else if (direction == JoyStick.STICK_UPLEFT) {
                                directionText.setText("Direction : Up Left");
                            } else if (direction == JoyStick.STICK_NONE) {
                                directionText.setText("Direction : Center");
                            }






                        } else if (arg1.getAction() == MotionEvent.ACTION_UP) {
                            directionText.setText("Direction :");
                        }
                        return true;
                    }

                });
            }

        });
    }

    private String transcriptionNodeId = null;

    private void updateTranscriptionCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();

        transcriptionNodeId = pickBestNodeId(connectedNodes);
    }

    private String pickBestNodeId(Set<Node> nodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node node : nodes) {
            if (node.isNearby()) {
                return node.getId();
            }
            bestNodeId = node.getId();
        }
        return bestNodeId;
    }




}