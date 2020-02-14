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
  iconUrl: "https://raw.githubusercontent.com/samuelkadolph/smart-home/master/icons/hourglass.png",
  iconX2Url: "https://raw.githubusercontent.com/samuelkadolph/smart-home/master/icons/hourglass@2x.png",
  iconX3Url: "https://raw.githubusercontent.com/samuelkadolph/smart-home/master/icons/hourglass@3x.png"
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
    section("What do you want to happen?") {
      input "onSwitches", "capability.switch", title: "Turn on these lights or switches", multiple: true, required: false
      input "offSwitches", "capability.switch", title: "Turn off these lights or switches", multiple: true, required: false
      input "dimmerLevel", "enum", title: "Set dimmers to this level", options: [10: "10%", 20: "20%", 30: "30%", 40: "40%", 50: "50%", 60: "60%", 70: "70%", 80: "80%", 90: "90%", 100: "100%"], required: false
    }

    // section("When do you want them to turn on?") {
      triggerPrefs("on")
    // }

    // section("When do you want them to turn off?") {
      triggerPrefs("off")
    // }

    // section("Turn these switches") {
    //   input "switches", "capability.switch", title: "Which switches?", multiple: true
    // }

    // section("On when") {
    //   triggerPrefs("on")
    // }

    // section("And turn these switches") {
    //   input "offSwitches", "enum", title: "Which switches?", options: ["same": "Same as above", "others": "Other switches"], defaultValue: "same", submitOnChange: true

    //   switch(offSwitches) {
    //     case "others":
    //       input "offSwitchesList", "capability.switch", title: "Which switches?", multiple: true
    //       break
    //     default:
    //       break
    //   }
    // }

    // section("Off when") {
    //   triggerPrefs("off")
    // }

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
  log.debug("Turning off ${offSwitches}")

  offSwitches.off()
}

def turnOn() {
  log.debug("Turning on ${onSwitches}")

  onSwitches.each { sw ->
    if (dimmerLevel != null && sw.hasCapability("Switch Level")) {
      sw.setLevel(dimmerLevel.toInteger())
    } else {
      sw.on()
    }
  }
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
  section("When do you want them to turn ${name}?") {
    input "${name}Trigger", "enum", title: "At", options: ["custom":"A Specific Time", "sunrise": "Sunrise", "sunset":"Sunset"], defaultValue: "custom", submitOnChange: true

    switch(settings["${name}Trigger"]) {
      case "sunrise":
        input "${name}SunriseOffset", "number", title: "Minutes after sunrise", defaultValue: 0
        break
      case "sunset":
        input "${name}SunsetOffset", "number", title: "Minutes after sunset", defaultValue: 0
        break
      default:
        input "${name}Time", "time", title: "When", required: true
    }
  }
}
