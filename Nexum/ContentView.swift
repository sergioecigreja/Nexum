//
//  ContentView.swift
//  Nexum
//
//  Created by Sérgio Igreja on 07/09/2024.
//

import SwiftUI
import Network

struct ContentView: View {
    
    let udpListener = UDPListener(on: NWEndpoint.Port(integerLiteral: 8088))
    
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello, world!")
            Button("heh", role: .destructive, action: sendNotification)
        }
        .padding()
    }
}

func sendNotification() {
    print("sending notification")
    let notificationService = NotificationService()
    notificationService.scheduleNotification(title: "title", body: "body", categoryIdentifier: "cId")
}

#Preview {
    ContentView()
}
