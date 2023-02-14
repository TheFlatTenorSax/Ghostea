package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;

import frc.robot.RobotContainer;


public class Drivetrain {

  private TalonFX right1;
  private TalonFX right2;
  private TalonFX left1;
  private TalonFX left2;

public Drivetrain() {
    right1 = new TalonFX(4);
    right2 = new TalonFX(2);
    left1 = new TalonFX(3);
    left2 = new TalonFX(1); 
    left2.follow(left1);
    right2.follow(right1);
    left1.setInverted(true);
    left2.setInverted(true);
    }
}
