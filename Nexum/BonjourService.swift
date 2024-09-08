//
//  BonjourService.swift
//  Nexum
//
//  Created by Sérgio Igreja on 07/09/2024.
//

import Foundation
import Network

class BonjourService {
    var service: NetService?
    
    func startService() {
        service = NetService(domain: "local.", type: "_http._tcp.", name: "NexumService", port: 8008)
        service?.publish()
    }
}

