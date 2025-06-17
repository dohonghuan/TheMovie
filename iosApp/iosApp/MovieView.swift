
import SwiftUI
import shared

struct MovieView: View {
    var movie: Movie
    var body: some View {
        VStack {
            AsyncImage(url: URL(string: movie.image ?? ""))
                .scaledToFill()
                  .frame(width: 300, height: 300, alignment: .center)
                  .clipped()
            
            Spacer()
            
            Text(movie.title)
            
            Text(movie.releaseDate ?? "")
        }
        
    }
}

