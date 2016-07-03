/*
 * Copyright (C) 2016 Team Ubercube
 *
 * This file is part of Ubercube.
 *
 *     Ubercube is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Ubercube is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Ubercube.  If not, see <http://www.gnu.org/licenses/>.
 */

package fr.veridiangames.core.game.entities.player;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.ECKeyMovement;
import fr.veridiangames.core.game.entities.components.ECMouseLook;
import fr.veridiangames.core.game.entities.components.ECRaycast;
import fr.veridiangames.core.game.entities.components.ECRigidbody;
import fr.veridiangames.core.game.entities.components.EComponent;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.EntityMovementPacket;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;
import fr.veridiangames.core.physics.colliders.AABoxCollider;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class ClientPlayer extends Player
{
	private NetworkableClient net;
	
	public ClientPlayer(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id, name, position, rotation, address, port);
		super.add(new ECRigidbody(this, position, rotation, new AABoxCollider(new Vec3(0.3f, 2.5f * 0.5f, 0.3f)), false));
		super.add(new ECKeyMovement(0.02f, 0.5f));
		super.add(new ECMouseLook(0.3f));
		super.add(new ECRaycast(5, 0.01f, "ClientPlayer", "Bullet"));
		super.addTag("ClientPlayer");
	}
	
	public void init(GameCore core)
	{
		super.init(core);
		this.getWeaponManager().getWeapon().setNet(net);
	}
	
	int time = 0;
	public void update(GameCore core)
	{
		super.update(core);
		time++;
		if (time % 60 == 5)
		{
			net.send(new EntityMovementPacket(this));
			time = 0;
		}
		
		if (this.getWeaponManager().getWeapon().hasPositionChanged())
		{
			this.getWeaponManager().getWeapon().setPositionChanged(false);
			net.send(new WeaponPositionPacket(this.getID(), this.getWeaponManager().getWeapon().getCurrentPosition()));
		}
	}
	
	public ECRaycast getRaycast()
	{
		return ((ECRaycast) super.get(EComponent.RAYCAST));
	}
	
	public ECKeyMovement getKeyComponent()
	{
		return ((ECKeyMovement) super.get(EComponent.KEY_MOVEMENT));
	}
	
	public ECMouseLook getMouseComponent()
	{
		return ((ECMouseLook) super.get(EComponent.MOUSE_LOOK));
	}

	public NetworkableClient getNet()
	{
		return net;
	}

	public void setNetwork(NetworkableClient net)
	{
		this.net = net;
	}
}