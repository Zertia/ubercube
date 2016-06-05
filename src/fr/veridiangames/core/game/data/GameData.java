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

package fr.veridiangames.core.game.data;

import fr.veridiangames.core.game.data.world.WorldGen;

/**
 * Created by Marccspro on 15 f�vr. 2016.
 */
public class GameData
{
	private WorldGen	worldGen;
	private int			worldSize;
	private float		viewDistance;

	public GameData()
	{
		worldSize = 100;
		worldGen = new WorldGen(5);
		//worldGen = new WorldGen(42 /*new Random().nextInt(99999)*/, worldSize);
		viewDistance = 150;
	}

	public int getWorldSize()
	{
		return worldSize;
	}

	public void setWorldSize(int worldSize)
	{
		this.worldSize = worldSize;
	}

	public float getViewDistance()
	{
		return viewDistance;
	}

	public void setViewDistance(float viewDistance)
	{
		this.viewDistance = viewDistance;
	}

	public WorldGen getWorldGen()
	{
		return worldGen;
	}
}