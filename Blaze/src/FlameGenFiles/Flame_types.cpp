/**
 * Autogenerated by Thrift Compiler (0.9.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#include "Flame_types.h"

#include <algorithm>

namespace Flame {

int _kMovementTypesValues[] = {
  MovementTypes::Forward,
  MovementTypes::Backward,
  MovementTypes::TurnLeft,
  MovementTypes::TurnRight,
  MovementTypes::Up,
  MovementTypes::Down
};
const char* _kMovementTypesNames[] = {
  "Forward",
  "Backward",
  "TurnLeft",
  "TurnRight",
  "Up",
  "Down"
};
const std::map<int, const char*> _MovementTypes_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(6, _kMovementTypesValues, _kMovementTypesNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

const char* Spark::ascii_fingerprint = "E86CACEB22240450EDCBEFC3A83970E4";
const uint8_t Spark::binary_fingerprint[16] = {0xE8,0x6C,0xAC,0xEB,0x22,0x24,0x04,0x50,0xED,0xCB,0xEF,0xC3,0xA8,0x39,0x70,0xE4};

uint32_t Spark::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->sparkID);
          this->__isset.sparkID = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Spark::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Spark");

  xfer += oprot->writeFieldBegin("sparkID", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32(this->sparkID);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(Spark &a, Spark &b) {
  using ::std::swap;
  swap(a.sparkID, b.sparkID);
  swap(a.__isset, b.__isset);
}

const char* Location::ascii_fingerprint = "6435B39C87AB0E30F30BEDEFD7328C0D";
const uint8_t Location::binary_fingerprint[16] = {0x64,0x35,0xB3,0x9C,0x87,0xAB,0x0E,0x30,0xF3,0x0B,0xED,0xEF,0xD7,0x32,0x8C,0x0D};

uint32_t Location::read(::apache::thrift::protocol::TProtocol* iprot) {

  uint32_t xfer = 0;
  std::string fname;
  ::apache::thrift::protocol::TType ftype;
  int16_t fid;

  xfer += iprot->readStructBegin(fname);

  using ::apache::thrift::protocol::TProtocolException;


  while (true)
  {
    xfer += iprot->readFieldBegin(fname, ftype, fid);
    if (ftype == ::apache::thrift::protocol::T_STOP) {
      break;
    }
    switch (fid)
    {
      case 1:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->x);
          this->__isset.x = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 2:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->y);
          this->__isset.y = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      case 3:
        if (ftype == ::apache::thrift::protocol::T_I32) {
          xfer += iprot->readI32(this->z);
          this->__isset.z = true;
        } else {
          xfer += iprot->skip(ftype);
        }
        break;
      default:
        xfer += iprot->skip(ftype);
        break;
    }
    xfer += iprot->readFieldEnd();
  }

  xfer += iprot->readStructEnd();

  return xfer;
}

uint32_t Location::write(::apache::thrift::protocol::TProtocol* oprot) const {
  uint32_t xfer = 0;
  xfer += oprot->writeStructBegin("Location");

  xfer += oprot->writeFieldBegin("x", ::apache::thrift::protocol::T_I32, 1);
  xfer += oprot->writeI32(this->x);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("y", ::apache::thrift::protocol::T_I32, 2);
  xfer += oprot->writeI32(this->y);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldBegin("z", ::apache::thrift::protocol::T_I32, 3);
  xfer += oprot->writeI32(this->z);
  xfer += oprot->writeFieldEnd();

  xfer += oprot->writeFieldStop();
  xfer += oprot->writeStructEnd();
  return xfer;
}

void swap(Location &a, Location &b) {
  using ::std::swap;
  swap(a.x, b.x);
  swap(a.y, b.y);
  swap(a.z, b.z);
  swap(a.__isset, b.__isset);
}

} // namespace