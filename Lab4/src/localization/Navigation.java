package localization;
//DPM Group 8
//Gareth Peters
//ID:260678626
//LuoQing(Ryan) Wang
//ID:260524744

import lejos.robotics.SampleProvider;

public class Navigation extends Thread{

	private Odometer odo;
	private static final int FORWARD_SPEED = 250;
	private static final int ROTATE_SPEED = 150;

	
	private boolean navigating = false;
	
	public void run(){
		
	}
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	public void travelTo(double x, double y) {
	this.navigating = true;
	// The first thing the ev3 does is turn to
	// the destination position and goes forward
	double deltaX = x - odo.getX();
	double deltaY = y - odo.getY();
	turnTo(Math.atan2(deltaX, deltaY));
	L4.getLeftMotor().setSpeed(200);
	L4.getRightMotor().setSpeed(200);
	L4.getLeftMotor().forward();
	L4.getRightMotor().forward();

	while (isNavigating()) {
		 //Redeclaration of values for when the Navigator avoids an obstacle
		deltaX = x - odo.getX();
		deltaY = y - odo.getY();	
		// After it clears the obstacle, turn
		// to the designated position and go forward
		if (Math.toDegrees(Math.atan2(deltaX, deltaY)) - odo.getTheta() > 3) {
					turnTo(Math.atan2(deltaX, deltaY));
		}
		if ((Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) <= 0.5) {
			L4.getLeftMotor().stop(true);
			L4.getRightMotor().stop(false);
			navigating = false;
			break;
		}
		L4.getLeftMotor().setSpeed(200);
		L4.getRightMotor().setSpeed(200);
		L4.getLeftMotor().forward();
		L4.getRightMotor().forward();
		
		// Checks first if the robot is within
		// a set circle around the position, if so,
		// break out of the method cause you've arrived
		
	}

}

	
	public void turnTo(double theta) {
		// Takes the difference in theta to see how much the robot must rotate.
		theta = Math.toDegrees(theta);
		double thetaNow = odo.getTheta();
		double thetaDifference = theta - thetaNow;
		// Minimizes the amount it needs to turn.
		while (thetaDifference <= -180) {
			thetaDifference += 360;
		} while (thetaDifference >= 180) {
			thetaDifference -= 360;
		}
		//Rotates to the desired angle
		L4.getLeftMotor().setSpeed(ROTATE_SPEED);
		L4.getRightMotor().setSpeed(ROTATE_SPEED);
		L4.getLeftMotor().rotate(convertAngle(L4.WHEEL_RADIUS, L4.TRACK, thetaDifference), true);
		L4.getRightMotor().rotate(-convertAngle(L4.WHEEL_RADIUS, L4.TRACK, thetaDifference), false);
	}
	
	
	public boolean isNavigating() {
		return this.navigating;
	}
	
	public static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	public static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
}
