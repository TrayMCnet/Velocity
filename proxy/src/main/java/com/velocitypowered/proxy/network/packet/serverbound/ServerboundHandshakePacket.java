/*
 * Copyright (C) 2018 Velocity Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.velocitypowered.proxy.network.packet.serverbound;

import com.google.common.base.MoreObjects;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.proxy.network.ProtocolUtils;
import com.velocitypowered.proxy.network.packet.Packet;
import com.velocitypowered.proxy.network.packet.PacketHandler;
import com.velocitypowered.proxy.network.packet.PacketReader;
import com.velocitypowered.proxy.network.packet.PacketWriter;
import io.netty.buffer.ByteBuf;

public class ServerboundHandshakePacket implements Packet {
  public static final PacketReader<ServerboundHandshakePacket> DECODER = (buf, version) -> {
    int realProtocolVersion = ProtocolUtils.readVarInt(buf);
    final ProtocolVersion protocolVersion = ProtocolVersion.byMinecraftProtocolVersion(realProtocolVersion);
    final String hostname = ProtocolUtils.readString(buf);
    final int port = buf.readUnsignedShort();
    final int nextStatus = ProtocolUtils.readVarInt(buf);
    return new ServerboundHandshakePacket(protocolVersion, hostname, port, nextStatus);
  };
  public static final PacketWriter<ServerboundHandshakePacket> ENCODER = PacketWriter.deprecatedEncode();

  private ProtocolVersion protocolVersion;
  private String serverAddress = "";
  private int port;
  private int nextStatus;

  public ServerboundHandshakePacket() {
  }

  public ServerboundHandshakePacket(final ProtocolVersion protocolVersion, final String hostname, final int port, final int nextStatus) {
    this.protocolVersion = protocolVersion;
    this.serverAddress = hostname;
    this.port = port;
    this.nextStatus = nextStatus;
  }

  @Override
  public void encode(ByteBuf buf, ProtocolVersion ignored) {
    ProtocolUtils.writeVarInt(buf, this.protocolVersion.protocol());
    ProtocolUtils.writeString(buf, this.serverAddress);
    buf.writeShort(this.port);
    ProtocolUtils.writeVarInt(buf, this.nextStatus);
  }

  @Override
  public boolean handle(PacketHandler handler) {
    return handler.handle(this);
  }

  public ProtocolVersion getProtocolVersion() {
    return protocolVersion;
  }

  @Deprecated
  public void setProtocolVersion(ProtocolVersion protocolVersion) {
    this.protocolVersion = protocolVersion;
  }

  public String getServerAddress() {
    return serverAddress;
  }

  @Deprecated
  public void setServerAddress(String serverAddress) {
    this.serverAddress = serverAddress;
  }

  public int getPort() {
    return port;
  }

  @Deprecated
  public void setPort(int port) {
    this.port = port;
  }

  public int getNextStatus() {
    return nextStatus;
  }

  @Deprecated
  public void setNextStatus(int nextStatus) {
    this.nextStatus = nextStatus;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
      .add("protocolVersion", this.protocolVersion)
      .add("serverAddress", this.serverAddress)
      .add("port", this.port)
      .add("nextStatus", this.nextStatus)
      .toString();
  }
}