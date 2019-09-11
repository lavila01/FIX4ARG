CREATE TABLE [dbo].[cotizaciones_rofex_fix](
	[symbol] [nvarchar](191) NOT NULL,
	[data] [nvarchar](max) NOT NULL,
	[fecha] [datetime] NOT NULL,
	[fix_message] [nvarchar](max) NOT NULL,
	[categoria] [nvarchar](191) NULL,
	[ultimo_operado] [float] NULL,
	[variacion] [float] NULL
)