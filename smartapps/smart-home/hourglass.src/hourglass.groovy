/*
 * Hourglass
 *
 * MIT License
 *
 * Copyright (c) 2019 Samuel Kadolph
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

definition(
  namespace: "smart-home",
  name: "Hourglass",
  author: "Samuel Kadolph",
  description: "Turn things on and off at when you want",
  category: "Safety & Security",
  iconUrl: "http://cdn.device-icons.smartthings.com/Appliances/appliances17-icn.png",
  iconX2Url: "http://cdn.device-icons.smartthings.com/Appliances/appliances17-icn@2x.png",
  iconX3Url: "http://cdn.device-icons.smartthings.com/Appliances/appliances17-icn@3x.png"
)

preferences {
  page(name: "prefPage")
}

def installed() {
  log.debug("installed() ${settings}")

  initialize()
}

def positionChangeHandler() {
  log.debug("positionChangeHandler()")

  unsubscribe()
  initialize()
}

def prefPage() {
  dynamicPage(name: "prefPage", install: true, uninstall: true) {
    section("Turn these devices") {
      input "devices", "capability.switch", title: "Which devices?", multiple: true
    }

    section("On when") {
      triggerPrefs("on")
    }

    section("And off when") {
      triggerPrefs("off")
    }

    section {
      label title: "Assign a name", required: false
      mode title: "Set for specific mode(s)", required: false
    }
  }
}

def sunriseTimeHandler(event) {
  log.debug("sunriseTimeHandler(${event})")

  handleSunrise(event.value)
}

def sunsetTimeHandler(event) {
  log.debug("sunsetTimeHandler(${event})")

  handleSunset(event.value)
}

def turnOff() {
  log.debug("Turning off ${devices}")

  devices.off()
}

def turnOn() {
  log.debug("Turning on ${devices}")

  devices.on()
}

def updated() {
  log.debug("updated() ${settings}")

  unsubscribe()
  initialize()
}

private def handleSunrise(String sunrise) {
  def sunriseTime = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", sunrise)

  if (onTrigger == "sunrise") {
    runOnce(new Date(sunriseTime.time + (onSunriseOffset * 60 * 1000)), turnOn)
  }

  if (offTrigger == "sunrise") {
    runOnce(new Date(sunriseTime.time + (offSunriseOffset * 60 * 1000)), turnOff)
  }
}

private def handleSunset(String sunset) {
  def sunsetTime = Date.parse("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", sunset)

  if (onTrigger == "sunset") {
    runOnce(new Date(sunsetTime.time + (onSunsetOffset * 60 * 1000)), turnOn)
  }

  if (offTrigger == "sunset") {
    runOnce(new Date(sunsetTime.time + (offSunsetOffset * 60 * 1000)), turnOff)
  }
}

private def initialize() {
  unschedule(turnOn)
  unschedule(turnOff)

  if (onTrigger == "sunrise" || offTrigger == "sunrise") {
    subscribe(location, "sunriseTime", sunriseTimeHandler)

    handleSunrise(location.currentValue("sunriseTime"))
  }

  if (onTrigger == "sunset" || offTrigger == "sunset") {
    subscribe(location, "sunsetTime", sunsetTimeHandler)

    handleSunset(location.currentValue("sunsetTime"))
  }

  if (onTrigger == "custom") {
    schedule(onTime, turnOn)
  }

  if (offTrigger == "custom") {
    schedule(offTime, turnOff)
  }

  subscribe(location, "position", positionChangeHandler)
}

private def triggerPrefs(String name) {
  input "${name}Trigger", "enum", title: "When?", options: ["custom":"Specific Time", "sunrise": "Sunrise", "sunset":"Sunset"], defaultValue: "custom", submitOnChange: true

  switch(settings["${name}Trigger"]) {
    case "custom":
      input "${name}Time", "time", title: "When", required: true
      break
    case "sunrise":
      input "${name}SunriseOffset", "number", title: "Minutes after sunrise", defaultValue: 0
      break
    case "sunset":
      input "${name}SunsetOffset", "number", title: "Minutes after sunset", defaultValue: 0
      break
  }
}
