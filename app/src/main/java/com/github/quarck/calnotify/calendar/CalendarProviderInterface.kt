//
//   Calendar Notifications Plus  
//   Copyright (C) 2016 Sergey Parshin (s.parshin.sc@gmail.com)
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation; either version 3 of the License, or
//   (at your option) any later version.
// 
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program; if not, write to the Free Software Foundation,
//   Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
//

package com.github.quarck.calnotify.calendar

import android.content.ContentResolver
import android.content.Context

interface CalendarProviderInterface {

    fun getAlertByTime(context: Context, alertTime: Long, skipDismissed: Boolean): List<EventAlertRecord>

    fun getAlertByEventIdAndTime(context: Context, eventId: Long, alertTime: Long): EventAlertRecord?

    fun getEventAlerts(context: Context, eventId: Long, startingAlertTime: Long, maxEntries: Int): List<EventAlertRecord>

    fun getEventReminders(context: Context, eventId: Long): List<EventReminderRecord>

    fun getEvent(context: Context, eventId: Long): EventRecord?

    fun dismissNativeEventAlert(context: Context, eventId: Long)

    fun cloneAndMoveEvent(context: Context, event: EventAlertRecord, addTime: Long): Long

    fun moveEvent(context: Context, event: EventAlertRecord, addTime: Long): Boolean

    fun getCalendars(context: Context): List<CalendarRecord>

    fun findNextAlarmTime(cr: ContentResolver, millis: Long): Long?

    fun getEventAlertsManually(context: Context, from: Long, to: Long): List<MonitorEventAlertEntry>

    fun isRepeatingEvent(context: Context, eventId: Long): Boolean?
}