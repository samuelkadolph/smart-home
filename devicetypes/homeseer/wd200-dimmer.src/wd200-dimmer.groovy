/*
 *  HomeSeer HS-WD100+
 *
 *
 *
 *
 *  Buttons
 *
 *
 *  Parameters
 *    Number  Description
 *    3       Bottom LED operation
 *    4       Paddle load orientation
 *    11      Ramp rate for remote control
 *    12      Ramp rate for local control
 *    13      Mode of operation
 *    14      Normal mode LED color
 *    21
 *    22
 *    23
 *    24
 *    25
 *    26
 *    27
 *    30
 *    31
 */

metadata {
  definition (name: "WD200+ Dimmer", namespace: "HomeSeer", author: "samuelkadolph") {
    capability "Actuator"
    capability "Button"
    capability "Configuration"
    capability "Indicator"
    capability "Polling"
    capability "Sensor"
    capability "Switch"
    capability "Switch Level"

    fingerprint mfr: "000C", prod: "4447", model: "3036"

    tiles(scale: 2) {
      multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
        tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
          attributeState "off", label: "OFF", action: "switch.on", icon: "st.Home.home30", backgroundColor: "#FFFFFF", nextState: "turningOn"
          attributeState "on", label: "ON", action: "switch.off", icon: "st.Home.home30", backgroundColor: "#00A0DC", nextState: "turningOff"
          attributeState "turningOn", label: "TURNINGON", action: "switch.off", icon: "st.Home.home30", backgroundColor:"#00A0DC", nextState: "turningOff"
          attributeState "turningOff", label: "TURNINGOFF", action: "switch.on", icon: "st.Home.home30", backgroundColor:"#FFFFFF", nextState: "turningOn"
        }
        tileAttribute ("device.level", key: "SLIDER_CONTROL") {
          attributeState "level", action: "switch level.setLevel"
        }
      }

      standardTile("tapUp1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲", action: "tapUp1", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapDown1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼", action: "tapDown1", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapUp2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲", action: "tapUp2", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapDown2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼", action: "tapDown2", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapUp3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲", action: "tapUp3", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapDown3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼", action: "tapDown3", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapUp4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲", action: "tapUp4", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapDown4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼", action: "tapDown4", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapUp5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲▲", action: "tapUp5", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("tapDown5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼▼", action: "tapDown5", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("holdUp", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold ▲", action: "holdUp", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }
      standardTile("holdDown", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold ▼", action: "holdDown", icon: "st.Home.home30", backgroundColor: "#FFFFFF"
      }

      standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
        state "default", label:'', action:"refresh.refresh", icon:"st.secondary.configure"
      }

      main "switch"
      details "switch", "tapUp1", "tapUp2", "tapUp3", "tapUp4", "tapUp5", "holdUp", "tapDown1", "tapDown2", "tapDown3", "tapDown4", "tapDown5", "holdDown", "refresh"
    }
  }
}

def configure() {

}

def off() {
  log.debug "off()"

  zwave.basicV1.basicSet(value: 0).format()
}

def on() {
  log.debug "on()"

  delayBetween([zwave.basicV1.basicSet(value: 0xFF).format(), zwave.switchMultilevelV1.switchMultilevelGet().format()], 5000)
  // zwave.basicV1.basicSet(value: 0xFF).format()
}

def poll() {
  zwave.switchMultilevelV1.switchMultilevelGet().format()
}

def setLevel(Number value) {
  log.debug "setLevel(${value})"

  [
    createEvent(name: "switch", value: (value > 0 ? "on" : "off")),
    createEvent(name: "level", value: value),
    zwave.basicV1.basicSet(value: level).format()
  ]
}

def parse(String description) {
  def cmd = zwave.parse(description)

  if (cmd) {
    return zwaveEvent(cmd)
  } else {
    return null
  }
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug "BasicReport ${cmd}"

  handleReport(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug "CentralSceneNotification ${cmd}"
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv1.SwitchMultilevelReport cmd) {
  log.debug "SwitchMultilevelReport ${cmd}"

  handleReport(cmd)
}

private handleReport(physicalgraph.zwave.Command cmd) {

}
