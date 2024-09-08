//
//  Listener.swift
//  Nexum
//
//  Created by Sérgio Igreja on 07/09/2024.
//

import Foundation
import Network

func startUDPListener() {
    let listener = try? NWListener(using: .udp, on: 8888)
    listener?.newConnectionHandler = { newConnection in
        newConnection.receiveMessage { (data, context, isComplete, error) in
            if let data = data, let message = String(data: data, encoding: .utf8) {
                print("Received discovery message: \(message)")
            }
        }
    }
}
