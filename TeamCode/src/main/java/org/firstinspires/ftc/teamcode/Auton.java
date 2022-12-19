package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;

@Autonomous(name = "Blue auton")
public class Auton extends LinearOpMode { //OpMode
    private final ElapsedTime runtime = new ElapsedTime();

    public static final int cpr = 1680; //Counts per Revolution
    public static final double cpi = cpr / (4 * Math.PI); //Counts per Inch

    public static final double turnRadius = 7.5; //pin-point turning radius, distance between left and right side / 2

    private DcMotorEx frontRight = null, backRight = null, backLeft = null, frontLeft = null, liftLeft = null, liftRight = null;
    private CRServo clawServo = null;

    //if we can't find the servo programmer we will need to set these values to something
    private double clawClosedPos = 0.0f;
    private double clawOpenPos = 1.0f;


    @Override
    public void runOpMode()
    {
        //do initialization stuff
        telemetry.addData("Status", "Initializing");

        frontRight = hardwareMap.get(DcMotorEx.class, "MotorC0");
        frontRight.setDirection(DcMotorEx.Direction.REVERSE);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        backRight = hardwareMap.get(DcMotorEx.class, "MotorC1");
        backRight.setDirection(DcMotorEx.Direction.FORWARD);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        backLeft = hardwareMap.get(DcMotorEx.class, "MotorC2");
        backLeft.setDirection(DcMotorEx.Direction.FORWARD);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        frontLeft = hardwareMap.get(DcMotorEx.class, "MotorC3");
        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftLeft = hardwareMap.get(DcMotorEx.class, "MotorE0");
        liftLeft.setDirection(DcMotorEx.Direction.FORWARD);
        liftLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        liftRight = hardwareMap.get(DcMotorEx.class, "MotorE1");
        liftRight.setDirection(DcMotorEx.Direction.FORWARD);
        liftRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        clawServo = hardwareMap.get(CRServo.class, "ServoClaw");
        clawServo.setDirection(CRServo.Direction.FORWARD);


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
//  cone at top right corner for blue and top left for red, initialize, close claw, strafe left, forward, 90 deg right (blue) or 90 degree left red, forward, open and close claw on cone, drive to terminal and drive backwards to substation
     /*   goForward(.5,48); //go forward 48 inches
        goSideways(0.5, 24); //go right 24 inches
        turn(0.5, 90);
        setLift(1.0);
        sleep(3000);
        setLift(0.0);*/
       /* setClaw(0.5);
      //  setClaw( 0.5);//both neg makes is close
        sleep(800);
        setClaw(0);
        setLift(-.05);
        sleep(500);
        goSideways(-0.5,20); //left strafe
        goForward(.5,10);//forward
        turn(-.5, 90);//right
        setClaw(0.5);
        sleep(500);
        setClaw(0);
       // setClaw(0.5);//both pos makes it open
        sleep(500);
        goForward(.5,10);//forward to cone
        setClaw(-0.5);
        sleep(500);
        setClaw(0);//both neg make it close
        sleep(500);
        goForward(.5,40);//forward to terminal
        setClaw(0.5);
        sleep(500);
        setClaw(0);//stop claw
        sleep(500);
        goForward(-.5,40);//back to substation*/
        goForward(-.5,30);
        sleep(900);
        //goForward(-.5, 10);



    }

    //average encoder ticks of all the motors, as long as the motors
    //are all spinning together and none of them get stuck this will
    //work as a good way to estimate the distance the bot has traveled
    private double avgTicks ()
    {
        return (double) (
                frontRight.getCurrentPosition() +
                        frontLeft.getCurrentPosition() +
                        backRight.getCurrentPosition() +
                        backLeft.getCurrentPosition())
                / 4.0;
    }

    //take abs before doing average calculation, in case some motors are going
    //one direction and other motors are going another, because that would make
    //the average 0
    private double absAvgTicks ()
    {
        return (double) (
                Math.abs(frontRight.getCurrentPosition()) +
                        Math.abs(frontLeft.getCurrentPosition()) +
                        Math.abs(backRight.getCurrentPosition()) +
                        Math.abs(backLeft.getCurrentPosition()) )
                / 4.0;
    }

    private void goForward (double speed, int in)
    {
        double sign;
        if (in > 0) sign = 1;
        else sign = -1;

        frontRight.setPower(sign * speed);
        backLeft.setPower(sign * speed);
        frontLeft.setPower(-sign * speed);
        backRight.setPower(-sign * speed);

        double target = in * cpi; //calculate target

        //use absAvgTicks() because 2 wheels are going forward and 2 are going
        //backward, so the average ticks will always be 0
        while(absAvgTicks() < Math.abs(target)); //wait until we reach target

        //stop
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        frontLeft.setPower(0);

        //reset the encoders and then make sure the runmode is RUN_USING_ENCODER
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

    }

    public void goSideways(double speed, int in) {
        double sign;
        if (in > 0) sign = 1;
        else sign = -1;

        //set motors to given speed and direction
        frontRight.setPower(sign * speed);
        backRight.setPower(sign * speed);
        backLeft.setPower(sign * speed);
        frontLeft.setPower(sign *speed);

        double target = in * cpi; //calculate target

        //using abs so that it goes both ways, this does mean that if we tell the bot
        //to go forward and it goes backward for some reason it will still stop after that
        //distance even thought it went the wrong direction
        //we'll have to make sure the motor direction is configured correctly
        while (Math.abs(avgTicks()) < Math.abs(target)) ;

        //stop
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        frontLeft.setPower(0);

        //reset the encoders and then make sure the runmode is RUN_USING_ENCODER
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    //currently unused
    private double avgRightTicks()
    {
        return (double) (frontRight.getCurrentPosition() + backRight.getCurrentPosition()) / 2.0;
    }

    //currently unused
    private double avgLeftTicks()
    {
        return (double) (frontLeft.getCurrentPosition() + backLeft.getCurrentPosition()) / 2.0;
    }

    //turn function, math is still kinda iffy so idk if this will work
    public void turn (double speed, int degrees)
    {
        double sign;
        if (degrees > 0) sign = 1;
        else sign = -1;

        //set motors to given speed and direction
        frontRight.setPower(sign * speed);
        backRight.setPower(sign * speed);
        backLeft.setPower(-sign * speed);
        frontLeft.setPower(-sign *speed);

        //use the arc length forula to convert degrees of turn to inches along the turning circle (the
        //length of the arc with a measure of the turn angle) and then convert inches to encoder ticks
        double target = degrees * (Math.PI / 180) * turnRadius * cpi;

        //then wait until we have moved by that many encoder ticks
        while(absAvgTicks() < Math.abs(target)) ;

        //stop
        frontRight.setPower(0);
        backRight.setPower(0);
        backLeft.setPower(0);
        frontLeft.setPower(0);

        //reset the encoders and then make sure the runmode is RUN_USING_ENCODER
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    //it might be better to have this set a position for the lift, so we could set
    //like a hight or something and the lift would go to that hight.
    //we would really only need to do that if we need to get to the lower junctions
    //in the auton
    public void setLift (double power)
    {
        liftLeft.setPower(power);
        liftRight.setPower(power);
    }

    /* public void setClaw (boolean open)
     {
         if (open) clawServo.setPosition(clawOpenPos);
         else clawServo.setPosition(clawClosedPos);
     }*/
    public void setClaw (double dir)
    {
        clawServo.setPower(dir * .75);
    }
}