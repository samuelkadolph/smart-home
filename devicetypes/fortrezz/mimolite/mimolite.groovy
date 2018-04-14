/*
 *  FortrezZ MIMOLite
 *
 *  https://www.fortrezz.com/io-modules
 */

metadata {
  definition (name: "MIMOLite", namespace: "FortrezZ", author: "samuelkadolph") {
    capability "Configuration"
    capability "Polling"
    capability "Switch"

    fingerprint mfr: "0084", prod: "0453", model: "0111", deviceJoinName: "MIMOLite"

    tiles(scale: 2) {
      standardTile("switch", "device.switch", width: 6, height: 4, canChangeIcon: true) {
        attributeState "off", label: "OFF", action: "switch.on", icon: "st.Home.home30", backgroundColor: "#FFFFFF", nextState: "turningOn"
        attributeState "on", label: "ON", action: "switch.off", icon: "st.Home.home30", backgroundColor: "#00A0DC", nextState: "turningOff"
        attributeState "turningOn", label: "TURNINGON", action: "switch.off", icon: "st.Home.home30", backgroundColor:"#00A0DC", nextState: "turningOff"
        attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", icon: "st.Home.home30", backgroundColor:"#FFFFFF", nextState: "turningOn"
      }

      main "switch"
      details "switch"
    }

    preferences {
      input "momentaryRelay", "enum", title: "Should the relay be latched or momentary?", options: ["Latched", "Momentary"], displayDuringSetup: true
      input "momentaryRelayDuration", "decimal", title: "How long should the relay stay on when set to momentary?\r\nRange: 0.1 to 25.5 seconds\r\nDefault: 1.0s", displayDuringSetup: true, range: "0.1..25.5"
    }
  }
}

def configure() {
  log.debug "configure()"
}

def off() {
  log.debug "off()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0x00).format()
  cmds << "delay 250"
  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  response(cmds)
}

def on() {
  log.debug "on()"

  def cmds = []

  cmds << zwave.basicV1.basicSet(value: 0xFF).format()
  cmds << "delay 250"
  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  response(cmds)
}

def poll() {
  def cmds = []

  cmds << zwave.switchMultilevelV1.switcBinaryGet().format()

  // TODO analog vs digital sensor read
  // cmds << 

  response(cmds)
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}