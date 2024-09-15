//
//  NotificationService.swift
//  Nexum
//
//  Created by Sérgio Igreja on 14/09/2024.
//

import Foundation
import UserNotifications

class NotificationService {
    
    let center = UNUserNotificationCenter.current()
    
    func scheduleNotification(title: String, body: String, categoryIdentifier: String) {
        center.requestAuthorization(options: [.sound, .alert], completionHandler: {res, err in
            print(res)
        })
        
        let content = UNMutableNotificationContent()
        content.title = title
        content.body = body
        content.categoryIdentifier = categoryIdentifier
        content.sound = UNNotificationSound.default
        
        center.removeAllPendingNotificationRequests()
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)
        center.add(request)
    }
    
    func checkForPermissions() {
        center.getNotificationSettings() { settings in
            switch settings.authorizationStatus {
            case .notDetermined:
                exit(0)
            case .denied:
                exit(0)
            case .authorized:
                exit(1)
            case .provisional:
                exit(1)
            case .ephemeral:
                exit(1)
            @unknown default:
                exit(1)
            }
        }
    }
}
