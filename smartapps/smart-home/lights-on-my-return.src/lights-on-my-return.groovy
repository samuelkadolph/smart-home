/*
 *  Lights On My Return
 *    SmartThings SmartApp that turns lights on when you get home and then off after a delay.
 *
 *  Copyright (c) 2018 Samuel Kadolph
 *
 *  Permission is hereby granted, free of charge, to any person obtaining
 *  a copy of this software and associated documentation files (the
 *  "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish,
 *  distribute, sublicense, and/or sell copies of the Software, and to
 *  permit persons to whom the Software is furnished to do so, subject to
 *  the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 *  LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 *  OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 *  WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

definition(
  name: "Lights On My Return",
  namespace: "smart-home",
  author: "Samuel Kadolph",
  description: "Turn on lights when you get home and off afterwards",
  category: "Safety & Security",
  iconUrl: "http://cdn.device-icons.smartthings.com/Lighting/light13-icn.png",
  iconX2Url: "http://cdn.device-icons.smartthings.com/Lighting/light13-icn@2x.png",
  iconX3Url: "http://cdn.device-icons.smartthings.com/Lighting/light13-icn@3x.png"
)

preferences {
  section("When one of these people comes home") {
    input "people", "capability.presenceSensor", multiple: true
  }

  section("Turn the following lights on") {
    input "lights", "capability.switch", multiple: true
  }

  section("And turn the lights off after") {
    input "delay", "decimal", title: "Number of minutes", required: false
  }
}

def installed() {
  log.debug("installed() ${settings}")

  attachHandlers()
}

def handlePresenceEvent(event) {
  log.debug("handlePresenceEvent(value:${event.value})")

  def lightsToTurnOff = []

  lights.each { light ->
    if (light.currentValue("switch") == "off") {
      log.debug("Turning on '${light}'")

      light.on()
      lightsToTurnOff << light
    }
  }

  if (lightsToTurnOff.size() > 0) {
    runIn(delay * 60, turnOffLight, [data: [lights: lightsToTurnOff]])
  }
}

def turnOffLights(data) {
  data.lights.each { light ->
    log.debug("Turning off '${light}'")

    light.off()
  }
}

def updated() {
  log.debug("updated() ${settings}")

  unsubscribe()
  attachHandlers()
}

private def attachHandlers() {
  subscribe(people, "presence", handlePresenceEvent)
}
