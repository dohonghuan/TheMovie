import SwiftUI
import shared

extension ContentView {
    enum ApiResult {
        case loading
        case result([Movie])
        case error(String)
    }

    @MainActor
    class ViewModel: ObservableObject {
        @Published var trendingMovies = ApiResult.loading
        
        init() {
            self.loadTrendingMovies()
        }
        
        func loadTrendingMovies() {
            Task {
                do {
                    self.trendingMovies = .loading
                    let trendingMovies = try await IosDataHelper.shared.appContainer.theMovieRepository.fetchTrendingMovies()
                    self.trendingMovies = .result(trendingMovies)
                } catch {
                    self.trendingMovies = .error(error.localizedDescription)
                }
            }
        }
    }
}

struct ContentView: View {

    @ObservedObject private(set) var viewModel: ViewModel

    var body: some View {
            NavigationView {
                listView()
                .navigationBarTitle("Trending movies")
                .navigationBarItems(trailing:
                    Button("Reload") {
                        self.viewModel.loadTrendingMovies()
                })
            }
        }

        private func listView() -> AnyView {
            switch viewModel.trendingMovies {
            case .loading:
                return AnyView(Text("Loading...").multilineTextAlignment(.center))
            case .result(let trendingMovies):
                return AnyView(List(trendingMovies, id: \.self) { movie in
                    MovieView(movie: movie)
                })
            case .error(let description):
                return AnyView(Text(description).multilineTextAlignment(.center))
            }
        }
}
