package com.thilenius.flame.service.thrift;

import java.util.List;

import org.apache.thrift.TException;

import com.thilenius.flame.service.thrift.genfiles.*;

public class FlameServiceHandler implements FlameService.Iface {

	@Override
	public Spark CreateSpark(Location worldLocation) throws TException {
		return null;
	}

	@Override
	public List<Spark> GetAllSparks() throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void RemoveSpark(Spark sparkToRemove) throws TException {
	}

	@Override
	public void RemoveAllSparks() throws TException {
	}

	@Override
	public boolean DispatchMovementCommand(Spark spark,
			MovementTypes movementCommand) throws TException {
		System.out.println("DispatchMovementCommand: " + movementCommand.toString());
		return true;
	}

	@Override
	public void DispatchOrientationCommand(Spark spark,
			OrientationTypes orientationCommand) throws TException {
		System.out.println("DispatchOrientationCommand: " + orientationCommand.toString());
	}
	
}
