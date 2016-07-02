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
 *     along with Ubercube.  If not, see http://www.gnu.org/licenses/.
 */

package fr.veridiangames.core.game.entities.player;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.game.entities.components.*;
import fr.veridiangames.core.game.entities.particles.ParticleSystem;
import fr.veridiangames.core.game.entities.particles.ParticlesBlood;
import fr.veridiangames.core.maths.Quat;
import fr.veridiangames.core.maths.Transform;
import fr.veridiangames.core.maths.Vec3;
import fr.veridiangames.core.maths.Vec3i;
import fr.veridiangames.core.network.NetworkableClient;
import fr.veridiangames.core.network.packets.EntityMovementPacket;
import fr.veridiangames.core.network.packets.WeaponPositionPacket;
import fr.veridiangames.core.physics.Rigidbody;
import fr.veridiangames.core.physics.colliders.AABoxCollider;
import fr.veridiangames.core.utils.Indexer;
import fr.veridiangames.core.utils.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marccspro on 3 f�vr. 2016.
 */
public class ClientPlayer extends Player
{
	private NetworkableClient net;

	private int life;

	private List<ParticleSystem> particleSystems;
	
	public ClientPlayer(int id, String name, Vec3 position, Quat rotation, String address, int port)
	{
		super(id, name, position, rotation, address, port);
		super.add(new ECRigidbody(this, position, rotation, new AABoxCollider(new Vec3(0.3f, 2.8f * 0.5f, 0.3f)), false));
		super.add(new ECKeyMovement(0.02f, 0.5f));
		super.add(new ECMouseLook(0.3f));
		super.add(new ECRaycast(5, 0.01f, "ClientPlayer", "Bullet", "ParticleSystem"));
		super.add(new ECDebug());
		super.addTag("ClientPlayer");

		this.life = 100;

		this.particleSystems = new ArrayList<>();
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

		if (this.life < 0) this.life = 0;
		if (this.life > 100) this.life = 100;

		if (this.getWeaponManager().getWeapon().hasPositionChanged())
		{
			this.getWeaponManager().getWeapon().setPositionChanged(false);
			net.send(new WeaponPositionPacket(this.getID(), this.getWeaponManager().getWeapon().getCurrentPosition()));
		}

		/** Debug **/
		if (this.getDebugComponent().isParticleSpawn())
		{
			particleSystems.add(new ParticlesBlood(Indexer.getUniqueID(), getPosition().copy())
					.setParticleVelocity(getRaycast().getDirection().copy().normalize().mul(0.05f))
					.setNetwork(net));
		}

        if (this.getDebugComponent().isParticleRemove())
        {
            for(int i = 0; i < particleSystems.size(); i++)
            {
                ParticleSystem p = particleSystems.get(i);
                p.destroy();
                //core.getGame().getEntityManager().remove(p.getID());
            }
            particleSystems.clear();
        }

		/* STEP */

		Vec3i block = new Vec3i(getPosition().copy().add(getRotation().getForward().copy().normalize()));
		if(getKeyComponent().isUp() && core.getGame().getWorld().getBlock(block.x, (int)getPosition().y - 1, block.z)!= 0){
            Rigidbody body = ((ECRigidbody)get(EComponent.RIGIDBODY)).getBody();
            if(body.isGrounded())
                body.applyForce(Vec3.UP, 0.1f);
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

	public ECDebug getDebugComponent()
	{
		return ((ECDebug) super.get(EComponent.DEBUG));
	}

	public NetworkableClient getNet()
	{
		return net;
	}

	public void setNetwork(NetworkableClient net)
	{
		this.net = net;
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}
}