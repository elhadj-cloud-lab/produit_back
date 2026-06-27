# produit_back

Microservice API REST pour la gestion des produits, catégories et images.

## Stack

- **Spring Boot** 3.5 · Java 21
- **API** : OpenAPI 3 (contrats générés depuis `src/main/resources/api/*.swagger.yaml`)
- **Mapping** : MapStruct
- **Base de données** : PostgreSQL (JPA / Hibernate)
- **Sécurité** : Spring Security — validation JWT (émis par `authentification-service`)
- **Tests** : JUnit 5 · Testcontainers · H2

## Endpoints

### Produits

| Méthode | URL | Rôle requis | Description |
|---|---|---|---|
| `GET` | `/api/produit` | `USER` / `ADMIN` | Liste tous les produits |
| `GET` | `/api/produit/{id}` | `USER` / `ADMIN` | Détail d'un produit |
| `GET` | `/api/produit/categorie/{id}` | `USER` / `ADMIN` | Produits par catégorie |
| `GET` | `/api/produit/search` | `USER` / `ADMIN` | Recherche par nom |
| `POST` | `/api/produit` | `ADMIN` | Créer un produit |
| `PUT` | `/api/produit/{id}` | `ADMIN` | Modifier un produit |
| `DELETE` | `/api/produit/{id}` | `ADMIN` | Supprimer un produit |

### Catégories

| Méthode | URL | Rôle requis | Description |
|---|---|---|---|
| `GET` | `/api/categorie` | `USER` / `ADMIN` | Liste toutes les catégories |
| `GET` | `/api/categorie/{id}` | `USER` / `ADMIN` | Détail d'une catégorie |
| `POST` | `/api/categorie` | `ADMIN` | Créer une catégorie |
| `PUT` | `/api/categorie/{id}` | `ADMIN` | Modifier une catégorie |
| `DELETE` | `/api/categorie/{id}` | `ADMIN` | Supprimer une catégorie |

### Images

| Méthode | URL | Description |
|---|---|---|
| `POST` | `/api/image/upload` | Upload d'une image |
| `GET` | `/api/image/{filename}` | Récupération d'une image |
| `DELETE` | `/api/image/{filename}` | Suppression d'une image |

## Sécurité

Le service **ne gère pas les logins** : il valide uniquement le JWT produit par `authentification-service`.

- Les lectures (`GET`) sont accessibles aux rôles `USER` et `ADMIN`
- Les mutations (`POST`, `PUT`, `DELETE`) requièrent le rôle `ADMIN`
- Les tokens révoqués sont détectés via la blacklist **Redis** partagée

## Variables d'environnement

| Variable | Défaut | Description |
|---|---|---|
| `JWT_SECRET` | `elhadjbs59@gmail.com` | Doit correspondre à celui de l'auth-service |
| `REDIS_HOST` | `localhost` | Host Redis |
| `REDIS_PORT` | `6379` | Port Redis |

La datasource est configurée directement dans `application.yaml` pour le dev local :

```yaml
spring.datasource.url: jdbc:postgresql://localhost:5432/produit
spring.datasource.username: postgres
spring.datasource.password: postgres
```

## Lancer en local

```bash
mvn spring-boot:run
```

Service disponible sur `http://localhost:8081`

## Tests

```bash
mvn test
```

Les tests d'intégration utilisent **Testcontainers** (PostgreSQL).

## Docker

```bash
# Build
docker build -t produit-back:local .

# Run
docker run --rm -p 8081:8081 \
  -e JWT_SECRET=dev-secret \
  produit-back:local
```

## Contrats OpenAPI

Les interfaces des contrôleurs sont générées automatiquement depuis :

- `src/main/resources/api/produit.swagger.yaml`
- `src/main/resources/api/categorie.swagger.yaml`

via le plugin Maven `openapi-generator-maven-plugin` (Spring target). Les DTOs et interfaces sont générés dans `target/generated-sources/openapi/`.

## CI/CD

Workflow : `.github/workflows/deploy.yaml`

- **PR → `develop`** : build + tests + docker build (sans push)
- **Push → `main`** : push image `dev-<sha>` sur GHCR + mise à jour GitOps DEV
- **Push → `prod`** : push image `prod-<sha>` + SBOM/provenance + mise à jour GitOps PROD

## Structure du code

```
src/main/java/com/bestech/produit/
├── controller/
│   ├── ProduitController.java       # Implémente ProduitApi (OpenAPI)
│   ├── CategorieController.java     # Implémente CategorieApi (OpenAPI)
│   └── ImageController.java
├── model/
│   ├── Produit.java
│   ├── Categorie.java
│   └── Image.java
├── repository/
│   ├── ProduitRepository.java
│   ├── CategorieRepository.java
│   └── ImageRepository.java
├── service/
│   ├── ProduitServiceImpl.java
│   ├── CategorieServiceImpl.java
│   └── ImageServiceImpl.java
├── bean/
│   └── ProduitBeanMapper.java       # MapStruct
└── security/
    ├── SecurityConfig.java
    └── JWTAuthorizationFilter.java
```
