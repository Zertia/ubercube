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

package fr.veridiangames.core.network;

import java.net.InetAddress;

import fr.veridiangames.core.GameCore;
import fr.veridiangames.core.network.packets.Packet;
import fr.veridiangames.server.server.tcp.NetworkServerTCP;
import fr.veridiangames.server.server.udp.NetworkServerUDP;

/**
 * Created by Marccspro on 26 f�vr. 2016.
 */
public interface NetworkableServer
{
	public GameCore getCore();

	public NetworkServerTCP getTcp();
	public NetworkServerUDP getUdp();

	public void log(String msg);

	public void tcpSend(Packet packet, InetAddress address, int port);
	public void tcpSendToAll(Packet packet);
	public void tcpSendToAny(Packet packet, int... ignores);

	public void udpSend(Packet packet, InetAddress address, int port);
	public void udpSendToAll(Packet data);
	public void udpSendToAny(Packet data, int... ignores);
}
