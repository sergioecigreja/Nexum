//
//  UDPListener.swift
//  Nexum
//
//  Created by Sérgio Igreja on 07/09/2024.
//

import Foundation
import Network
import Combine

class UDPListener: ObservableObject {
    var listener: NWListener?
    var connection: NWConnection?
    var queue = DispatchQueue.global(qos: .userInitiated)
    ///Observers can subscribe to this data buffer
    @Published private(set) public var messageReceived: Data?
    /// When NWConnection is actively listening this will be true
    @Published private(set) public var isReady: Bool = false
    /// Will become false if the UDPListener ceases to listen
    @Published public var listening: Bool = true
    
    init(on port: NWEndpoint.Port) {
        let params = NWParameters.udp
        params.allowFastOpen = true
        
        self.listener = try? NWListener(using: params, on: port)
        self.listener?.stateUpdateHandler = { update in
            switch update {
            case .ready:
                self.isReady = true
                print("Listener connected to port \(port)")
            case .failed, .cancelled:
                self.listening = false
                self.isReady = false
                print("Listener disconnected from port \(port)")
            default:
                print("Listener connecting to port \(port)")
            }
        }
        
        self.listener?.newConnectionHandler = { connection in
            print("Listener receiving new messages")
            self.createConnection(connection: connection)
        }
        self.listener?.start(queue: self.queue)
    }
    
    func createConnection(connection: NWConnection) {
        self.connection = connection
        self.connection?.stateUpdateHandler = { (newState) in
            switch newState {
            case .ready:
                print("Listener ready to receive message - \(connection)")
                self.receive()
            case .cancelled, .failed:
                print("Listener failed to receive message - \(connection)")
                self.cancel()
            default:
                print("Listener waiting to receive message -\(connection)")
            }
        }
        self.connection?.start(queue: .global())
    }
    
    func receive() {
        self.connection?.receiveMessage {data, context, isComplete, error in
            if let unwrappedError = error {
                print("Error: NWError received in \(#function) - \(unwrappedError)")
                return
            }
            
            guard isComplete, let data = data else {
                print("Error: Received nil Data with context - \(String(describing: context))")
                return
            }
            
            self.messageReceived = data
            let str = String(decoding: data, as: UTF8.self)
            print("Received data - \(str)")
            if self.listening {
                self.receive()
            }
        }
    }
    
    func cancel() {
        self.listening = false
        self.connection?.cancel()
    }
    
    
}
