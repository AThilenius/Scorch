/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.thilenius.flame.service.thrift.genfiles;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum OrientationTypes implements org.apache.thrift.TEnum {
  TurnLeft(1),
  TurnRight(2);

  private final int value;

  private OrientationTypes(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static OrientationTypes findByValue(int value) { 
    switch (value) {
      case 1:
        return TurnLeft;
      case 2:
        return TurnRight;
      default:
        return null;
    }
  }
}
