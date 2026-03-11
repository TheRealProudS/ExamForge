import SwiftUI

struct SplashView: View {
    @EnvironmentObject var appState: AppState

    @State private var logoOpacity:    Double = 0
    @State private var logoScale:      Double = 0.8
    @State private var taglineOpacity: Double = 0
    @State private var taglineOffset:  Double = 20

    var body: some View {
        ZStack {
            Color.brandPrimary.ignoresSafeArea()

            VStack(spacing: 24) {
                Image(systemName: "graduationcap.fill")
                    .font(.system(size: 88))
                    .foregroundColor(.white)
                    .opacity(logoOpacity)
                    .scaleEffect(logoScale)

                Text("ExamForge")
                    .font(.largeTitle.bold())
                    .foregroundColor(.white)
                    .opacity(logoOpacity)

                Text("IHK Pr\u{00fc}fungsvorbereitung")
                    .font(.subheadline)
                    .foregroundColor(.white.opacity(0.85))
                    .opacity(taglineOpacity)
                    .offset(y: taglineOffset)
            }
        }
        .onAppear {
            withAnimation(.easeOut(duration: 0.7)) {
                logoOpacity = 1
                logoScale   = 1
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.3)) {
                taglineOpacity = 1
                taglineOffset  = 0
            }
            DispatchQueue.main.asyncAfter(deadline: .now() + 2.1) {
                withAnimation(.easeInOut(duration: 0.4)) {
                    appState.showSplash = false
                }
            }
        }
    }
}
