import SwiftUI
import shared

struct ContentView: View {
	var body: some View {
		ComposeView()
            .ignoresSafeArea(.keyboard)
	}
}

struct ComposeView: UIViewControllerRepresentable {
    
    func makeUIViewController(context: Context) ->  UIViewController {
        MainIOSKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}
