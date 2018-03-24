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

    command "holdDown1"
    command "holdUp1"
    command "tapDown1"
    command "tapDown2"
    command "tapDown3"
    command "tapDown4"
    command "tapDown5"
    command "tapUp1"
    command "tapUp2"
    command "tapUp3"
    command "tapUp4"
    command "tapUp5"

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

      valueTile("tapUp1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap   ▲  ", action: "tapUp1", backgroundColor: "#FFFFFF"
      }
      valueTile("holdUp", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold   ▲  ", action: "holdUp", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap  ▲▲ ", action: "tapUp2", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲", action: "tapUp3", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲", action: "tapUp4", backgroundColor: "#FFFFFF"
      }
      valueTile("tapUp5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▲▲▲▲▲", action: "tapUp5", backgroundColor: "#FFFFFF"
      }

      valueTile("tapDown1", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap   ▼  ", action: "tapDown1", backgroundColor: "#FFFFFF"
      }
      valueTile("holdDown", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Hold   ▼  ", action: "holdDown", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown2", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap  ▼▼ ", action: "tapDown2", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown3", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼", action: "tapDown3", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown4", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼", action: "tapDown4", backgroundColor: "#FFFFFF"
      }
      valueTile("tapDown5", "device.button", width: 1, height: 1, decoration: "flat") {
        state "default", label: "Tap ▼▼▼▼▼", action: "tapDown5", backgroundColor: "#FFFFFF"
      }

      standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
        state "default", label:'', action:"refresh.refresh", icon:"st.secondary.configure"
      }

      main "switch"
      details "switch", "tapUp1", "holdUp", "tapUp2", "tapUp3", "tapUp4", "tapUp5", "tapDown1", "holdDown", "tapDown2", "tapDown3", "tapDown4", "tapDown5", "refresh"
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

  def onoff = value > 0 ? "on" : "off"

  [
    createEvent(name: "switch", value: onoff),
    createEvent(name: "level", value: value),
    response(zwave.basicV1.basicSet(value: level).format())
  ]
}

def holdDown1() {
  log.debug "holdDown1()"
}

def holdUp1() {
  log.debug "holdUp1()"
}

def tapDown1() {
  log.debug "tapDown1()"
}

def tapDown2() {
  log.debug "tapDown2()"
}

def tapDown3() {
  log.debug "tapDown3()"
}

def tapDown4() {
  log.debug "tapDown4()"
}

def tapDown5() {
  log.debug "tapDown5()"
}

def tapUp1() {
  log.debug "tapUp1()"
}

def tapUp2() {
  log.debug "tapUp2()"
}

def tapUp3() {
  log.debug "tapUp3()"
}

def tapUp4() {
  log.debug "tapUp4()"
}

def tapUp5() {
  log.debug "tapUp5()"
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
  log.debug cmd

  handleReport(cmd)
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug cmd
}

def zwaveEvent(physicalgraph.zwave.commands.switchmultilevelv3.SwitchMultilevelReport cmd) {
  log.debug cmd

  handleReport(cmd)
}

private handleReport(physicalgraph.zwave.Command cmd) {

}
